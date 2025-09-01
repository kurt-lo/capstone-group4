import React, { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  Legend,
} from "recharts";

function Dashboard() {
  const [containers, setContainers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedContainer, setSelectedContainer] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const COLORS = ["#6366F1", "#22D3EE", "#F472B6", "#34D399"];

  // Format date function
  function formatDate(dateString) {
    if (!dateString) return "";
    const date = new Date(dateString);
    const datePart = date.toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
    });
    const timePart = date.toLocaleTimeString("en-US", {
      hour: "numeric",
      minute: "numeric",
      hour12: true,
    });
    return `${datePart} ${timePart}`;
  }

  // Add Container
  const [formData, setFormData] = useState({
    containerType: "",
    origin: "",
    destination: "",
    weight: "",
    containerSize: "",
    departureDate: "",
    arrivalDate: "",
    createdBy: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    try {
      console.log("New Container:", formData);
      // TODO: send to backend
    } catch (error) {
    } finally {
      setIsModalOpen(false);
      setFormData({
        containerType: "",
        origin: "",
        destination: "",
        weight: "",
        containerSize: "",
        departureDate: "",
        arrivalDate: "",
      });
    }
  };

  // Fetch containers
  useEffect(() => {
    const fetchContainers = async () => {
      try {
        setError(null);
        setLoading(true);

        const response = await fetch("http://localhost:9090/api/containers");
        if (!response.ok) throw new Error(`HTTP ${response.status}`);

        const data = await response.json();
        setContainers(data);
      } catch (e) {
        console.error("Failed to fetch containers:", e);
        setError("Failed to load containers. Please check the backend.");
      } finally {
        setLoading(false);
      }
    };

    fetchContainers();
  }, []);

  // CHART DATA

  const containerTypes = containers.reduce((acc, c) => {
    acc[c.containerType] = (acc[c.containerType] || 0) + 1;
    return acc;
  }, {});
  const typeData = Object.entries(containerTypes).map(([name, value]) => ({
    name,
    value,
  }));

  const weightData = containers.map((c, idx) => ({
    name: `C${idx + 1}`,
    weight: c.weight,
  }));

  const containerSizeData = containers.map((c) => ({
    origin: `O${c.origin}`,
    destination: `D${c.destination}`,
    size: parseInt(c.containerSize.replace(/\D/g, "")) || 0,
  }));

  // Stat Cards
  const totalContainers = containers.length;
  const totalWeight = containers.reduce((sum, c) => sum + c.weight, 0);
  const avgWeight =
    totalContainers > 0 ? (totalWeight / totalContainers).toFixed(1) : 0;

  if (loading) return <div>Loading containers…</div>;
  if (error) return <div style={{ color: "crimson" }}>{error}</div>;

  return (
    <div className="p-5 bg-gray-900 min-h-screen ">
      {/* Layout Wrapper to allow squeezing */}
      <div className="flex h-screen">
        <div className="flex flex-col gap-6 w-full">
          <motion.div
            layout
            className={`flex-1 flex flex-col gap-6 transition-all duration-500 `}
          >
            {/* Top Stats */}
            <div className="grid grid-cols-3 gap-6 mb-8">
              <motion.div
                whileHover={{ scale: 1.05 }}
                className="bg-gradient-to-br from-cyan-500 to-blue-500 p-6 rounded-2xl shadow-lg"
              >
                <h2 className="text-lg">Total Containers</h2>
                <p className="text-3xl font-bold">{totalContainers}</p>
              </motion.div>
              <motion.div
                whileHover={{ scale: 1.05 }}
                className="bg-gradient-to-br from-green-500 to-emerald-500 p-6 rounded-2xl shadow-lg"
              >
                <h2 className="text-lg">Total Weight</h2>
                <p className="text-3xl font-bold">{totalWeight} kg</p>
              </motion.div>
              <motion.div
                whileHover={{ scale: 1.05 }}
                className="bg-gradient-to-br from-purple-500 to-pink-500 p-6 rounded-2xl shadow-lg"
              >
                <h2 className="text-lg">Avg Weight</h2>
                <p className="text-3xl font-bold">{avgWeight} kg</p>
              </motion.div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
              {/* PIE CHART */}
              <div className="rounded-2xl border border-purple-500/40 shadow-lg shadow-purple-500/20 bg-white/5 backdrop-blur-md p-6">
                <h2 className="text-lg font-semibold mb-4 text-white">
                  Container Types
                </h2>
                <ResponsiveContainer width="100%" height={250}>
                  <PieChart>
                    <Pie
                      data={typeData}
                      dataKey="value"
                      nameKey="name"
                      cx="50%"
                      cy="50%"
                      outerRadius={90}
                      innerRadius={50}
                      paddingAngle={5}
                    >
                      {typeData.map((_, i) => (
                        <Cell
                          key={i}
                          fill={COLORS[i % COLORS.length]}
                          stroke="none"
                        />
                      ))}
                    </Pie>
                    <Tooltip />
                    <Legend />
                  </PieChart>
                </ResponsiveContainer>
              </div>

              {/* LINE CHART */}
              <div className="rounded-2xl border border-cyan-500/40 shadow-lg shadow-cyan-500/20 bg-white/5 backdrop-blur-md p-6">
                <h2 className="text-lg font-semibold mb-4 text-white">
                  Weight Trend
                </h2>
                <ResponsiveContainer width="100%" height={250}>
                  <LineChart data={weightData}>
                    <defs>
                      <linearGradient
                        id="lineGradient"
                        x1="0"
                        y1="0"
                        x2="0"
                        y2="1"
                      >
                        <stop
                          offset="5%"
                          stopColor="#22D3EE"
                          stopOpacity={0.9}
                        />
                        <stop
                          offset="95%"
                          stopColor="#22D3EE"
                          stopOpacity={0.1}
                        />
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                    <XAxis dataKey="name" stroke="#94a3b8" />
                    <YAxis stroke="#94a3b8" />
                    <Tooltip />
                    <Line
                      type="monotone"
                      dataKey="weight"
                      stroke="url(#lineGradient)"
                      strokeWidth={3}
                      dot={{ r: 5, fill: "#22D3EE" }}
                    />
                  </LineChart>
                </ResponsiveContainer>
              </div>

              {/* BAR CHART */}
              <div className="rounded-2xl border border-pink-500/40 shadow-lg shadow-pink-500/20 bg-white/5 backdrop-blur-md p-6">
                <h2 className="text-lg font-semibold mb-4 text-white">
                  Container Sizes
                </h2>
                <ResponsiveContainer width="100%" height={250}>
                  <BarChart data={containerSizeData}>
                    <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                    <XAxis dataKey="origin" stroke="#94a3b8" />
                    <YAxis stroke="#94a3b8" />
                    <Tooltip />
                    <Bar dataKey="size" radius={[8, 8, 0, 0]}>
                      {containerSizeData.map((_, i) => (
                        <Cell key={i} fill={COLORS[i % COLORS.length]} />
                      ))}
                    </Bar>
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </div>
            {/* Table + Title + Add Button */}
            <motion.div
              layout
              animate={{ marginTop: selectedContainer ? 20 : 0 }}
              transition={{ type: "spring", stiffness: 100, damping: 20 }}
              className="flex-1 p-5 rounded-2xl border-1 bg-gray-700/30 border-cyan-700 shadow-[0_0_20px_2px_rgba(34,211,238,0.8)] overflow-x-auto"
            >
              {/* Title + Button Row */}
              <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl font-bold text-white">My Containers</h1>
                <button
                  onClick={() => setIsModalOpen(true)}
                  className="bg-green-600 hover:bg-green-500 text-white font-semibold px-4 py-2 rounded-lg shadow-lg transition"
                >
                  Add Container
                </button>
              </div>

              {/* Table */}
              {containers.length === 0 ? (
                <div className="text-gray-400">No containers found.</div>
              ) : (
                <motion.table
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: 30 }}
                  transition={{ duration: 0.6, ease: "easeOut" }}
                  className="w-full table-auto text-left"
                >
                  <thead>
                    <tr className="bg-gray-700/0 text-white">
                      <th className="border-b border-blue-gray-100 p-5">
                        Container ID
                      </th>
                      <th className="border-b border-blue-gray-100 p-5">
                        Type
                      </th>
                      <th className="border-b border-blue-gray-100 p-5">
                        Origin
                      </th>
                      <th className="border-b border-blue-gray-100 p-5">
                        Destination
                      </th>
                      <th className="border-b border-blue-gray-100 p-5">
                        Weight
                      </th>
                      <th className="border-b border-blue-gray-100 p-5">
                        Size
                      </th>
                      <th className="border-b border-blue-gray-100 p-5">
                        Departure Date
                      </th>
                      <th className="border-b border-blue-gray-100 p-5">
                        Arrival Date
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {containers.map((container, index) => (
                      <motion.tr
                        key={container.containerId + index}
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ delay: index * 0.05, duration: 0.4 }}
                        className="bg-gray-900/30 hover:bg-gray-800/40 transition-colors backdrop-blur-sm cursor-pointer"
                        onClick={() => setSelectedContainer(container)}
                      >
                        <td className="p-5 border-b border-gray-700 text-white">
                          {container.containerId}
                        </td>
                        <td className="p-5 border-b border-gray-700 text-white">
                          {container.containerType}
                        </td>
                        <td className="p-5 border-b border-gray-700 text-white">
                          {container.origin_city}, {container.origin_country}
                        </td>
                        <td className="p-5 border-b border-gray-700 text-white">
                          {container.destination_city},{" "}
                          {container.destination_country}
                        </td>
                        <td className="p-5 border-b border-gray-700 text-white">
                          {container.weight}
                        </td>
                        <td className="p-5 border-b border-gray-700 text-white">
                          {container.containerSize}
                        </td>
                        <td className="p-5 border-b border-gray-700 text-white">
                          {formatDate(container.departureDate)}
                        </td>
                        <td className="p-5 border-b border-gray-700 text-white">
                          {formatDate(container.arrivalDate)}
                        </td>
                      </motion.tr>
                    ))}
                  </tbody>
                </motion.table>
              )}
            </motion.div>
          </motion.div>
        </div>

        {/* Details Card */}
        <AnimatePresence>
          {selectedContainer && (
            <motion.div
              key="details-card"
              initial={{ x: "100%", opacity: 0 }}
              animate={{ x: 0, opacity: 1 }}
              exit={{ x: "100%", opacity: 0 }}
              transition={{ type: "spring", stiffness: 80, damping: 20 }}
              layout
              className="mx-6 w-[20%] bg-gray-900/60 backdrop-blur-xl rounded-2xl shadow-xl border border-cyan-400/60 p-6 text-white flex-shrink-0"
            >
              {/* Close Button */}
              <div className="flex justify-between items-center mb-4">
                <h2 className="text-xl font-bold">Container Details</h2>
                <button
                  onClick={() => setSelectedContainer(null)}
                  className="text-gray-400 hover:text-white transition"
                >
                  ✕
                </button>
              </div>

              {/* Container details */}
              <div className="space-y-3">
                {[
                  { label: "ID", value: selectedContainer.containerId },
                  { label: "Type", value: selectedContainer.containerType },
                  {
                    label: "Origin",
                    value: `${selectedContainer.origin_city}, ${selectedContainer.origin_country}`,
                  },
                  {
                    label: "Destination",
                    value: `${selectedContainer.destination_city}, ${selectedContainer.destination_country}`,
                  },
                  { label: "Weight", value: selectedContainer.weight },
                  { label: "Size", value: selectedContainer.containerSize },
                  {
                    label: "Departure",
                    value: formatDate(selectedContainer.departureDate),
                  },
                  {
                    label: "Arrival",
                    value: formatDate(selectedContainer.arrivalDate),
                  },
                  { label: "Created By", value: selectedContainer.createdBy },
                  { label: "Updated By", value: selectedContainer.updatedBy },
                ].map((item, i) => (
                  <motion.p
                    key={i}
                    initial={{ opacity: 0, x: 20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: i * 0.05 }}
                  >
                    <strong>{item.label}:</strong> {item.value}
                  </motion.p>
                ))}
              </div>
            </motion.div>
          )}
        </AnimatePresence>

        {/* Add Container Modal */}
        <AnimatePresence>
          {isModalOpen && (
            <motion.div
              className="fixed inset-0 bg-black/60 backdrop-blur-md flex justify-center items-center z-50"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
            >
              <motion.div
                initial={{ scale: 0.8, opacity: 0 }}
                animate={{ scale: 1, opacity: 1 }}
                exit={{ scale: 0.8, opacity: 0 }}
                transition={{ type: "spring", stiffness: 100, damping: 15 }}
                className="bg-gray-900 w-[60%] max-w-3xl rounded-2xl shadow-lg p-8 border border-cyan-400/50 text-white"
              >
                <div className="flex justify-between items-center mb-6">
                  <h2 className="text-2xl font-bold">Add Container</h2>
                  <button
                    onClick={() => setIsModalOpen(false)}
                    className="text-cyan-400 hover:text-cyan-200"
                  >
                    ✕
                  </button>
                </div>

                <form
                  onSubmit={handleSubmit}
                  className="grid grid-cols-2 gap-6"
                >
                  <div>
                    <label className="block text-sm mb-2">Container Type</label>
                    <input
                      type="text"
                      name="containerType"
                      value={formData.containerType}
                      onChange={handleChange}
                      className="w-full p-2 rounded bg-gray-800 border border-gray-600 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm mb-2">Origin</label>
                    <input
                      type="text"
                      name="origin"
                      value={formData.origin}
                      onChange={handleChange}
                      className="w-full p-2 rounded bg-gray-800 border border-gray-600 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm mb-2">Destination</label>
                    <input
                      type="text"
                      name="destination"
                      value={formData.destination}
                      onChange={handleChange}
                      className="w-full p-2 rounded bg-gray-800 border border-gray-600 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm mb-2">Weight</label>
                    <input
                      type="number"
                      name="weight"
                      value={formData.weight}
                      onChange={handleChange}
                      className="w-full p-2 rounded bg-gray-800 border border-gray-600 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm mb-2">Size</label>
                    <input
                      type="text"
                      name="containerSize"
                      value={formData.containerSize}
                      onChange={handleChange}
                      className="w-full p-2 rounded bg-gray-800 border border-gray-600 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm mb-2">Departure Date</label>
                    <input
                      type="date"
                      name="departureDate"
                      value={formData.departureDate}
                      onChange={handleChange}
                      className="w-full p-2 rounded bg-gray-800 border border-gray-600 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm mb-2">Arrival Date</label>
                    <input
                      type="date"
                      name="arrivalDate"
                      value={formData.arrivalDate}
                      onChange={handleChange}
                      className="w-full p-2 rounded bg-gray-800 border border-gray-600 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div className="col-span-2 flex justify-end">
                    <button
                      type="submit"
                      className="px-6 py-2 bg-green-600 hover:bg-green-500 rounded-lg shadow-md text-white font-medium"
                    >
                      Save Container
                    </button>
                  </div>
                </form>
              </motion.div>
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    </div>
  );
}

export default Dashboard;
