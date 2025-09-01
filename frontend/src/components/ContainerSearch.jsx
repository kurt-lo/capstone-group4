import { useState } from "react";
import axios from "axios";

const ContainerSearch = () => {
  const [filters, setFilters] = useState({
    containerType: "",
    originId: "",
    destinationId: "",
    status: "",
    startDate: "",
    endDate: "",
  });

  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const handleSearch = async () => {
    setLoading(true);
    setError("");

    try {
      const params = {};

      // Add normal search params
      if (filters.containerType) params.containerType = filters.containerType;
      if (filters.originId) params.originId = filters.originId;
      if (filters.destinationId) params.destinationId = filters.destinationId;
      if (filters.status) params.status = filters.status;

      // Add optional date range if both startDate and endDate exist
      if (filters.startDate && filters.endDate && filters.originId) {
        params.locationId = filters.originId; // assuming originId as location
        params.startDate = new Date(filters.startDate).toISOString();
        params.endDate = new Date(filters.endDate).toISOString();
        // Call the day-range API
        const response = await axios.get("http://localhost:9090/containers/day-range", { params });
        setResults(response.data);
        setLoading(false);
        return;
      }

      // Otherwise call general search
      const response = await axios.get("http://localhost:9090/containers/search", { params });
      setResults(response.data);
    } catch (err) {
      setError("Failed to fetch containers.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-4 space-y-6">
      <h2 className="text-xl font-bold">Search Containers</h2>

      <div className="grid grid-cols-2 gap-4">
        <input
          type="text"
          name="containerType"
          placeholder="Container Type"
          value={filters.containerType}
          onChange={handleChange}
          className="border rounded p-2"
        />
        <input
          type="number"
          name="originId"
          placeholder="Origin ID"
          value={filters.originId}
          onChange={handleChange}
          className="border rounded p-2"
        />
        <input
          type="number"
          name="destinationId"
          placeholder="Destination ID"
          value={filters.destinationId}
          onChange={handleChange}
          className="border rounded p-2"
        />
        <select
          name="status"
          value={filters.status}
          onChange={handleChange}
          className="border rounded p-2"
        >
        <option value="">-- Select Status --</option>
        <option value="DISCHARGED">DISCHARGED</option>
        <option value="VANNING">VANNING</option>
        <option value="IN_TRANSIT">IN_TRANSIT</option>
        <option value="DEVANNING">DEVANNING</option>
        <option value="RECEIVED">RECEIVED</option>
        <option value="OPEN">OPEN</option>
        </select>

        {/* Start and End Date */}
        <input
          type="datetime-local"
          name="startDate"
          value={filters.startDate}
          onChange={handleChange}
          className="border rounded p-2"
        />
        <input
          type="datetime-local"
          name="endDate"
          value={filters.endDate}
          onChange={handleChange}
          className="border rounded p-2"
        />
      </div>

      <button
        onClick={handleSearch}
        className="px-4 py-2 bg-blue-500 text-white rounded"
      >
        {loading ? "Searching..." : "Search"}
      </button>

      <div>
        {error && <p className="text-red-500">{error}</p>}
        <table className="min-w-full border mt-4">
          <thead>
            <tr>
              <th className="border px-2 py-1">ID</th>
              <th className="border px-2 py-1">Type</th>
              <th className="border px-2 py-1">Origin</th>
              <th className="border px-2 py-1">Destination</th>
              <th className="border px-2 py-1">Status</th>
              <th className="border px-2 py-1">Departure Date</th>
            </tr>
          </thead>
          <tbody>
            {results.length > 0 ? (
              results.map((c) => (
                <tr key={c.containerId}>
                  <td className="border px-2 py-1">{c.containerId}</td>
                  <td className="border px-2 py-1">{c.containerType}</td>
                  <td className="border px-2 py-1">{c.origin?.name}</td>
                  <td className="border px-2 py-1">{c.destination?.name}</td>
                  <td className="border px-2 py-1">{c.status}</td>
                  <td className="border px-2 py-1">{c.departureDate}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" className="text-center py-2">
                  No containers found
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ContainerSearch;
