import React, { useEffect } from "react";
import { motion } from "framer-motion";
import { jsPDF } from "jspdf";
import { autoTable } from "jspdf-autotable";
import {
  Search,
  Package,
  Truck,
  Ship,
  MapPin,
  Filter,
  Plus,
  Upload,
  Download,
  Settings,
  Bell,
  BarChart3,
  ShieldCheck,
} from "lucide-react";
import { useState } from "react";
import axios from "axios";
import useAuthStore from "../authentication/useAuthStore";

export default function ReportPage() {
  const token = useAuthStore((state) => state.user?.token);
  const [filters, setFilters] = useState({
    originId: "",
    destinationId: "",
    startDate: "",
    endDate: "",
  });

  const [data, setData] = useState([]);
  const [city, setCity] = useState([]);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  // Fetch report data
  const fetchData = async () => {
    setLoading(true);
    setErr("");

    // Format to LocalDateTime (no timezone, no Z)
    const toLocalDateTime = (input) => {
      const d = new Date(input);
      const pad = (n, s = 2) => String(n).padStart(s, "0");
      return [
        `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`,
        `T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(
          d.getSeconds()
        )}.${pad(d.getMilliseconds(), 3)}`,
      ].join("");
    };

    const params = new URLSearchParams();
    if (filters.originId) params.append("originId", filters.originId);
    if (filters.destinationId)
      params.append("destinationId", filters.destinationId);
    if (filters.startDate)
      params.append("startDate", toLocalDateTime(filters.startDate));
    if (filters.endDate)
      params.append("endDate", toLocalDateTime(filters.endDate));

    try {
      const url = `http://localhost:9090/api/containers/search-by-origin?${params.toString()}`;
      const res = await axios.get(url, {
        headers: { Authorization: `Bearer ${token}` },
      });
      console.log(url);
      console.log(res.data);
      setData(res.data || []);
    } catch (e) {
      setErr(e?.response?.data?.message || e.message || "Request failed");
    } finally {
      setLoading(false);
    }
  };

  //fetch cities
  const fetchCities = async () => {
    try {
      setErr(null);
      setLoading(true);

      const fetchCities = await axios.get("http://localhost:9090/api/city", {
        headers: { Authorization: `Bearer ${token}` },
      });

      const data = fetchCities.data;
      setCity(data);
    } catch (e) {
      console.error("Failed to fetch cities:", e);
      setErr("Failed to load cities. Please check the backend.");
    } finally {
      setLoading(false);
    }
  };

  // Fetch cities
  useEffect(() => {
    fetchCities();
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    fetchData();
  };

  const handleExportPDF = () => {
    const doc = new jsPDF({
      orientation: "landscape",
      unit: "pt",
      format: "A4",
    });

    // Title
    const title = "Recent Containers";
    doc.setFont("helvetica", "bold");
    doc.setFontSize(16);
    const pageWidth = doc.internal.pageSize.getWidth();
    const textWidth = doc.getTextWidth(title);
    doc.text(title, (pageWidth - textWidth) / 2, 40);

    // Optional subtitle (timestamp)
    doc.setFont("helvetica", "normal");
    doc.setFontSize(10);
    doc.text(`Generated: ${new Date().toISOString()}`, 40, 60);

    // Table
    const head = [
      ["Container ID", "Type", "Status", "Origin", "Destination", "ETA"],
    ];
    const body = (data || []).map((row) => [
      row.containerId ?? row.id ?? "-",
      row.containerType ?? "-",
      row.status ?? "Active",
      formatPlace(row.originCity, row.originCountry),
      formatPlace(row.destinationCity, row.destinationCountry),
      formatDate(row.arrivalDate),
    ]);

    autoTable(doc, {
      startY: 80,
      head,
      body,
      theme: "grid",
      styles: {
        font: "helvetica",
        fontSize: 10,
        cellPadding: 6,
        overflow: "linebreak",
        valign: "middle",
      },
      headStyles: {
        fillColor: [243, 244, 246], // light gray-ish (approx daisyUI)
        textColor: 20,
        lineColor: [209, 213, 219],
        lineWidth: 0.5,
        halign: "left",
        fontStyle: "bold",
      },
      bodyStyles: {
        lineColor: [229, 231, 235],
        lineWidth: 0.5,
      },
      columnStyles: {
        0: { cellWidth: 90 }, // Container ID
        1: { cellWidth: 80 }, // Type
        2: { cellWidth: 80 }, // Status
        3: { cellWidth: 180 }, // Origin
        4: { cellWidth: 180 }, // Destination
        5: { cellWidth: 120 }, // ETA
      },
      didDrawPage: (dataArg) => {
        // Footer
        const p = doc.internal.getNumberOfPages();
        doc.setFontSize(9);
        doc.text(
          `Page ${p}`,
          pageWidth - 60,
          doc.internal.pageSize.getHeight() - 20
        );
      },
    });

    // Option A: download immediately
    doc.save("containers.pdf");

    // Option B: get a Blob (if you want to preview/open yourself)
    // const pdfBlob = doc.output("blob");
    // const url = URL.createObjectURL(pdfBlob);
    // window.open(url, "_blank");
  };

  // Add these helpers above your return()
  const formatPlace = (city, country) => {
    if (city && country) return `${city}, ${country}`;
    return city || country || "-";
  };

  const formatDate = (s) => {
    if (!s) return "-";
    const d = new Date(s);
    // Example: 2025-09-02 02:35
    const pad = (n) => String(n).padStart(2, "0");
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(
      d.getDate()
    )} ${pad(d.getHours())}:${pad(d.getMinutes())}`;
  };

  const formatDateISOZ = (s) => {
    if (!s) return "-";
    const d = new Date(s);
    return d.toISOString();
  };

  return (
    <div className="p-4 max-w-4xl mx-auto">
      <h2 className="text-xl font-semibold mb-3">Search Containers</h2>

      <form
        onSubmit={handleSubmit}
        className="grid grid-cols-1 md:grid-cols-2 gap-3 mb-4"
      >
        <div>
          <label className="block text-sm mb-1">Origin ID</label>
          <select
            name="originId"
            value={filters.originId}
            onChange={handleChange}
            className="w-full bg-gray-900 text-white border border-gray-700 rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Select Origin City</option>
            {city.map((c) => (
              <option
                key={c.id}
                value={c.id}
                className="bg-gray-900 text-white"
              >
                {c.cityName}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="block text-sm mb-1">Destination ID</label>
          <select
            name="destinationId"
            value={filters.destinationId}
            onChange={handleChange}
            className="w-full bg-gray-900 text-white border border-gray-700 rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Select Destination City</option>
            {city.map((c) => (
              <option
                key={c.id}
                value={c.id}
                className="bg-gray-900 text-white"
              >
                {c.cityName}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="block text-sm mb-1">Start Date</label>
          <input
            name="startDate"
            value={filters.startDate}
            onChange={handleChange}
            type="datetime-local"
            className="w-full border rounded px-3 py-2 bg-gray-900 border-gray-700"
          />
        </div>

        <div>
          <label className="block text-sm mb-1">End Date</label>
          <input
            name="endDate"
            value={filters.endDate}
            onChange={handleChange}
            type="datetime-local"
            className="w-full border rounded px-3 py-2 bg-gray-900 border-gray-700"
          />
        </div>

        <div className="md:col-span-2 flex flex-row-reverse gap-2">
          <button
            type="button"
            className="px-4 py-2 rounded border btn btn-default cursor-pointer"
            onClick={() => {
              setFilters({
                originId: "",
                destinationId: "",
                startDate: "",
                endDate: "",
              });
              setData([]);
              setErr("");
            }}
          >
            Clear
          </button>
          <button
            type="submit"
            className="px-4 py-2 rounded btn btn-primary"
            disabled={loading}
          >
            {loading ? "Searching..." : "Search"}
          </button>
        </div>
      </form>

      {err && <p className="text-red-600 mb-3">{err}</p>}
      <button
        type="button"
        onClick={handleExportPDF}
        className="px-4 py-2 rounded border btn btn-outline cursor-pointer"
        disabled={!data?.length}
      >
        Export PDF
      </button>

      {data.length > 0 ? (
        <section className="container mx-auto px-4 py-6">
          <div className="card bg-base-100 shadow border border-base-300 overflow-hidden">
            <div className="card-body">
              <div className="flex items-center justify-between gap-4">
                <h2 className="card-title">Containers</h2>
                <div className="join">
                  <input
                    className="input input-bordered input-sm join-item"
                    placeholder="Search table…"
                    // optional: implement local filter
                    onChange={(e) => {
                      const term = e.target.value.toLowerCase();
                      setData((prev) =>
                        prev.filter(
                          (row) =>
                            row.containerId.toString().includes(term) ||
                            row.containerType?.toLowerCase().includes(term) ||
                            row.originCity?.toLowerCase().includes(term) ||
                            row.destinationCity?.toLowerCase().includes(term)
                        )
                      );
                    }}
                  />
                  <button className="btn btn-sm join-item btn-ghost">
                    <Search className="size-4" />
                  </button>
                </div>
              </div>

              <div className="overflow-x-auto mt-3 rounded-lg border border-base-300">
                <table className="table table-zebra">
                  <thead>
                    <tr>
                      <th>Container ID</th>
                      <th>Type</th>
                      {/* <th>Status</th> */}
                      <th>Origin</th>
                      <th>Destination</th>
                      <th>ETA</th>
                    </tr>
                  </thead>
                  <tbody>
                    {data.map((row) => (
                      <tr key={row.containerId}>
                        <td className="font-mono font-semibold">
                          {row.containerId}
                        </td>
                        <td>{row.containerType}</td>
                        {/* <td>
                          <div className="badge badge-outline badge-primary">
                            {row.status || "Active"}
                          </div>
                        </td> */}
                        <td className="truncate max-w-[10rem]">
                          {row.originCity}, {row.originCountry}
                        </td>
                        <td className="truncate max-w-[10rem]">
                          {row.destinationCity}, {row.destinationCountry}
                        </td>
                        <td>{row.arrivalDate?.replace("T", " ")}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </section>
      ) : (
        !loading && <p className="text-sm text-gray-600">No results.</p>
      )}
    </div>
  );
}
