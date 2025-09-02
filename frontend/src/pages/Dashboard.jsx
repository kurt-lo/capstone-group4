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
import axios from "axios";
import { toast } from "react-toastify";
import useAuthStore from "../authentication/useAuthStore";
import Sidebar from "../components/Sidebar";
import ProgressBar from "../components/ProgressBar";
import { ChevronLeft, ChevronRight } from "lucide-react";

function Dashboard() {
  const [containers, setContainers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedContainer, setSelectedContainer] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isUpdateModalOpen, setIsUpdateModalOpen] = useState(false);
  const [trackingEvent, setTrackingEvent] = useState([]);
  const [city, setCity] = useState([]);

  const token = useAuthStore((state) => state.user?.token);

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

  // Pagination state variables
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [searchTerm, setSearchTerm] = useState("");

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

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        `http://localhost:9090/api/containers/create`,
        formData,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(response.data);
      console.log("New Container:", formData);
      toast.success("Container created successfully!");
      fetchContainers();
    } catch (error) {
      toast.error("Error creating container!");
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

  // UPDATE CONTAINER
  const handleUpdateChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleUpdate = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.put(
        `http://localhost:9090/api/containers/${selectedContainer.containerId}`,
        formData,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );
      toast.success("Container updated successfully!");
      fetchContainers();
    } catch (error) {
      toast.error("Error updating container!");
    } finally {
      setIsUpdateModalOpen(false);
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

  // DELETE CONTAINER
  const handleDelete = async (id) => {
    try {
      const response = await axios.delete(
        `http://localhost:9090/api/containers/${id}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      toast.success(response.body);
    } catch (error) {
      toast.error(response.body);
    }
  };

  // GET ALL CONTAINERS
  const fetchContainers = async () => {
    try {
      setError(null);
      setLoading(true);

      const fetchContainers = await axios.get(
        "http://localhost:9090/api/containers",
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      const data = fetchContainers.data;
      console.log(data);
      setContainers(data);
    } catch (e) {
      console.error("Failed to fetch containers:", e);
      setError("Failed to load containers. Please check the backend.");
    } finally {
      setLoading(false);
    }
  };

  // GET TRACKING EVENTS PER CONTAINER
  const fetchTrackingEvents = async (id) => {
    try {
      console.log(id);
      const response = await axios.get(
        `http://localhost:9090/api/trackingEvent/container/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setTrackingEvent(response?.data);
      console.log(response?.data);
    } catch (error) {
      console.error(error);
    }
  };

  //fetch cities
  const fetchCities = async () => {
    try {
      setError(null);
      setLoading(true);

      const fetchCities = await axios.get("http://localhost:9090/api/city", {
        headers: { Authorization: `Bearer ${token}` },
      });

      const data = fetchCities.data;
      setCity(data);
    } catch (e) {
      console.error("Failed to fetch cities:", e);
      setError("Failed to load cities. Please check the backend.");
    } finally {
      setLoading(false);
    }
  };

  // Fetch cities and containers
  useEffect(() => {
    fetchCities();
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

  // Filtered containers based on search term
  const filteredContainers = containers.filter((container) =>
    container.containerType.toLowerCase().includes(searchTerm.toLowerCase()) ||
    container.originCity.toLowerCase().includes(searchTerm.toLowerCase()) ||
    container.destinationCity.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Pagination calculations
  const totalPages = Math.ceil(filteredContainers.length / itemsPerPage);
  const startIndex = (currentPage - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const currentContainers = filteredContainers.slice(startIndex, endIndex);

  // Pagination handlers
  const goToPage = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const goToPrevious = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  const goToNext = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  // Reset to first page when search term changes
  useEffect(() => {
    setCurrentPage(1);
  }, [searchTerm]);

  if (loading) return <div className="flex items-center justify-center min-h-screen text-white">Loading containers…</div>;
  if (error) return <div className="flex items-center justify-center min-h-screen text-red-500">{error}</div>;

  return (
    <div className="flex lg:flex-row bg-gray-900 min-h-screen">
      <Sidebar />
      
      {/* Main Content */}
      <div className="flex-1 p-2 sm:p-4 lg:p-6 overflow-x-hidden">
        <div className="max-w-full">
          
          {/* Top Stats - Responsive Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-6 mb-6 lg:mb-8">
            <motion.div
              whileHover={{ scale: 1.02 }}
              className="bg-gradient-to-br from-cyan-500 to-blue-500 p-4 sm:p-6 rounded-2xl shadow-lg"
            >
              <h2 className="text-sm sm:text-lg">Total Containers</h2>
              <p className="text-xl sm:text-3xl font-bold">{totalContainers}</p>
            </motion.div>
            <motion.div
              whileHover={{ scale: 1.02 }}
              className="bg-gradient-to-br from-green-500 to-emerald-500 p-4 sm:p-6 rounded-2xl shadow-lg"
            >
              <h2 className="text-sm sm:text-lg">Total Weight</h2>
              <p className="text-xl sm:text-3xl font-bold">{totalWeight} kg</p>
            </motion.div>
            <motion.div
              whileHover={{ scale: 1.02 }}
              className="bg-gradient-to-br from-purple-500 to-pink-500 p-4 sm:p-6 rounded-2xl shadow-lg sm:col-span-2 lg:col-span-1"
            >
              <h2 className="text-sm sm:text-lg">Avg Weight</h2>
              <p className="text-xl sm:text-3xl font-bold">{avgWeight} kg</p>
            </motion.div>
          </div>

          {/* Charts - Stack on mobile, grid on larger screens */}
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-4 lg:gap-6 mb-6 lg:mb-10">
                        {/* PIE CHART */}
            <div className="rounded-2xl border border-purple-500/40 shadow-lg shadow-purple-500/20 bg-white/5 backdrop-blur-md p-4 sm:p-6">
              <h2 className="text-base lg:text-lg font-semibold mb-4 text-white">
                Container Types
              </h2>
              <ResponsiveContainer width="100%" height={200}>
                <PieChart>
                  <Pie
                    data={typeData}
                    dataKey="value"
                    nameKey="name"
                    cx="50%"
                    cy="45%"
                    outerRadius={60}
                    innerRadius={35}
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
                  <Tooltip 
                    contentStyle={{
                      backgroundColor: 'rgba(255, 255, 255, 0.95)',
                      border: '1px solid #6366F1',
                      borderRadius: '8px',
                      color: '#000000'
                    }}
                    labelStyle={{ color: '#000000' }}
                  />
                  <Legend 
                    verticalAlign="bottom"
                    height={36}
                    wrapperStyle={{
                      paddingTop: '20px',
                      fontSize: '12px'
                    }}
                  />
                </PieChart>
              </ResponsiveContainer>
            </div>

            {/* LINE CHART */}
            <div className="rounded-2xl border border-cyan-500/40 shadow-lg shadow-cyan-500/20 bg-white/5 backdrop-blur-md p-4 sm:p-6">
              <h2 className="text-base lg:text-lg font-semibold mb-4 text-white">
                Weight Trend
              </h2>
              <ResponsiveContainer width="100%" height={200}>
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
                  <XAxis dataKey="name" stroke="#94a3b8" fontSize={12} />
                  <YAxis stroke="#94a3b8" fontSize={12} />
                  <Tooltip
                    contentStyle={{
                      backgroundColor: 'rgba(255, 255, 255, 0.95)',
                      border: '1px solid #22D3EE',
                      borderRadius: '8px',
                      color: '#000000'
                    }}
                    labelStyle={{ color: '#000000' }}
                  />
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
            <div className="rounded-2xl border border-pink-500/40 shadow-lg shadow-pink-500/20 bg-white/5 backdrop-blur-md p-4 sm:p-6">
              <h2 className="text-base lg:text-lg font-semibold mb-4 text-white">
                Container Sizes
              </h2>
              <ResponsiveContainer width="100%" height={200}>
                <BarChart data={containerSizeData}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                  <XAxis dataKey="origin" stroke="#94a3b8" fontSize={12} />
                  <YAxis stroke="#94a3b8" fontSize={12} />
                  <Tooltip
                    contentStyle={{
                      backgroundColor: 'rgba(255, 255, 255, 0.95)',
                      border: '1px solid #22D3EE',
                      borderRadius: '8px',
                      color: '#000000'
                    }}
                    labelStyle={{ color: '#000000' }}
                  />
                  <Bar dataKey="size" radius={[8, 8, 0, 0]}>
                    {containerSizeData.map((_, i) => (
                      <Cell key={i} fill={COLORS[i % COLORS.length]} />
                    ))}
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>

          {/* Container Table Section */}
          <div className="flex flex-col xl:flex-row gap-4 lg:gap-6">
            {/* Table Container */}
            <motion.div
              layout
              className="flex-1 p-3 sm:p-5 rounded-2xl bg-gray-700/30 border border-cyan-700 shadow-[0_0_20px_2px_rgba(34,211,238,0.8)]"
            >
              {/* Header with controls */}
              <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-4 gap-4">
                <h1 className="text-xl sm:text-2xl font-bold text-white">My Containers</h1>

                <div className="flex flex-col sm:flex-row items-stretch sm:items-center gap-2 sm:gap-4 w-full sm:w-auto">
                  {/* Search Input */}
                  <div className="relative flex-1 sm:flex-initial">
                    <input
                      type="text"
                      placeholder="Search containers..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                      className="w-full pl-10 pr-4 py-2 rounded-lg bg-gray-800 border border-gray-600 text-white focus:border-cyan-400 text-sm"
                    />
                    <svg
                      className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                      />
                    </svg>
                  </div>

                  {/* Items per page selector */}
                  <select
                    value={itemsPerPage}
                    onChange={(e) => setItemsPerPage(Number(e.target.value))}
                    className="px-3 py-2 rounded-lg bg-gray-800 border border-gray-600 text-white focus:border-cyan-400 text-sm"
                  >
                    <option value={5}>5 per page</option>
                    <option value={10}>10 per page</option>
                    <option value={20}>20 per page</option>
                    <option value={50}>50 per page</option>
                  </select>

                  <button
                    onClick={() => setIsModalOpen(true)
                    }
                    className="bg-green-600 hover:bg-green-500 text-white font-semibold px-4 py-2 rounded-lg shadow-lg transition cursor-pointer text-sm whitespace-nowrap"
                  >
                    Add Container
                  </button>
                </div>
              </div>

              {/* Table with horizontal scroll on mobile */}
              {filteredContainers.length === 0 ? (
                <div className="text-gray-400 text-center py-8">
                  {searchTerm
                    ? "No containers match your search."
                    : "No containers found."}
                </div>
              ) : (
                <>
                  <div className="overflow-x-auto">
                    <div className="max-h-96 overflow-y-auto scrollbar-thin scrollbar-thumb-cyan-500 scrollbar-track-gray-700">
                      <motion.table
                        initial={{ opacity: 0, y: 30 }}
                        animate={{ opacity: 1, y: 0 }}
                        exit={{ opacity: 0, y: 30 }}
                        transition={{ duration: 0.6, ease: "easeOut" }}
                        className="w-full table-auto text-left min-w-[800px]"
                      >
                        <thead className="sticky top-0 bg-gray-800/90 backdrop-blur-sm">
                          <tr className="text-white">
                            <th className="border-b border-gray-600 p-2 sm:p-4 text-xs sm:text-sm">Container ID</th>
                            <th className="border-b border-gray-600 p-2 sm:p-4 text-xs sm:text-sm">Type</th>
                            <th className="border-b border-gray-600 p-2 sm:p-4 text-xs sm:text-sm">Origin</th>
                            <th className="border-b border-gray-600 p-2 sm:p-4 text-xs sm:text-sm">Destination</th>
                            <th className="border-b border-gray-600 p-2 sm:p-4 text-xs sm:text-sm">Weight</th>
                            <th className="border-b border-gray-600 p-2 sm:p-4 text-xs sm:text-sm">Size</th>
                            <th className="border-b border-gray-600 p-2 sm:p-4 text-xs sm:text-sm">Departure</th>
                            <th className="border-b border-gray-600 p-2 sm:p-4 text-xs sm:text-sm">Arrival</th>
                            <th className="border-b border-gray-600 p-2 sm:p-4 text-xs sm:text-sm">Actions</th>
                          </tr>
                        </thead>
                        <tbody>
                          {currentContainers.map((container, index) => (
                            <motion.tr
                              key={container.containerId + index}
                              initial={{ opacity: 0, y: 10 }}
                              animate={{ opacity: 1, y: 0 }}
                              transition={{ delay: index * 0.05, duration: 0.4 }}
                              className="bg-gray-900/30 hover:bg-gray-800/40 transition-colors backdrop-blur-sm cursor-pointer"
                              onClick={() => {
                                setSelectedContainer(container);
                                fetchTrackingEvents(container.containerId);
                              }}
                            >
                              <td className="p-2 sm:p-4 border-b border-gray-700 text-white text-xs sm:text-sm">
                                {container.containerId}
                              </td>
                              <td className="p-2 sm:p-4 border-b border-gray-700 text-white text-xs sm:text-sm">
                                {container.containerType}
                              </td>
                              <td className="p-2 sm:p-4 border-b border-gray-700 text-white text-xs sm:text-sm">
                                {container.originCity}, {container.originCountry}
                              </td>
                              <td className="p-2 sm:p-4 border-b border-gray-700 text-white text-xs sm:text-sm">
                                {container.destinationCity}, {container.destinationCountry}
                              </td>
                              <td className="p-2 sm:p-4 border-b border-gray-700 text-white text-xs sm:text-sm">
                                {container.weight}
                              </td>
                              <td className="p-2 sm:p-4 border-b border-gray-700 text-white text-xs sm:text-sm">
                                {container.containerSize}
                              </td>
                              <td className="p-2 sm:p-4 border-b border-gray-700 text-white text-xs sm:text-sm">
                                {formatDate(container.departureDate)}
                              </td>
                              <td className="p-2 sm:p-4 border-b border-gray-700 text-white text-xs sm:text-sm">
                                {formatDate(container.arrivalDate)}
                              </td>
                              <td className="p-2 sm:p-4 border-b border-gray-700 text-white text-xs sm:text-sm">
                                <div className="flex flex-col sm:flex-row gap-1 sm:gap-2">
                                  <button
                                    onClick={(e) => {
                                      e.stopPropagation();
                                      setIsUpdateModalOpen(true);
                                      setSelectedContainer(container);
                                      setFormData(container);
                                    }}
                                    className="px-2 py-1 bg-blue-600 hover:bg-blue-500 rounded text-xs transition"
                                  >
                                    Update
                                  </button>
                                  <button
                                    className="px-2 py-1 bg-red-600 hover:bg-red-500 rounded text-xs transition"
                                    onClick={(e) => {
                                      e.stopPropagation();
                                      document.getElementById("my_modal_5").showModal();
                                    }}
                                  >
                                    Delete
                                  </button>
                                </div>
                              </td>
                            </motion.tr>
                          ))}
                        </tbody>
                      </motion.table>
                    </div>
                  </div>

                  {/* Pagination Controls - Mobile friendly */}
                  <div className="flex flex-col sm:flex-row justify-between items-center mt-6 gap-4">
                    <div className="text-xs sm:text-sm text-gray-400 text-center sm:text-left">
                      Showing {startIndex + 1} to {Math.min(endIndex, filteredContainers.length)} of {filteredContainers.length} containers
                    </div>

                    <div className="flex items-center space-x-1 sm:space-x-2">
                      <button
                        onClick={goToPrevious}
                        disabled={currentPage === 1}
                        className="p-2 rounded-lg border border-gray-600 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-700 transition-colors text-white"
                      >
                        <ChevronLeft size={14} />
                      </button>

                      <div className="flex space-x-1">
                        {Array.from({ length: Math.min(totalPages, 3) }, (_, i) => {
                          let pageNumber;
                          if (totalPages <= 3) {
                            pageNumber = i + 1;
                          } else if (currentPage <= 2) {
                            pageNumber = i + 1;
                          } else if (currentPage >= totalPages - 1) {
                            pageNumber = totalPages - 2 + i;
                          } else {
                            pageNumber = currentPage - 1 + i;
                          }

                          return (
                            <button
                              key={pageNumber}
                              onClick={() => goToPage(pageNumber)}
                              className={`px-2 sm:px-3 py-1 rounded-lg text-xs sm:text-sm transition-colors ${currentPage === pageNumber
                                  ? "bg-cyan-600 text-white"
                                  : "border border-gray-600 text-white hover:bg-gray-700"
                                }`}
                            >
                              {pageNumber}
                            </button>
                          );
                        })}
                      </div>

                      <button
                        onClick={goToNext}
                        disabled={currentPage === totalPages}
                        className="p-2 rounded-lg border border-gray-600 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-700 transition-colors text-white"
                      >
                        <ChevronRight size={14} />
                      </button>
                    </div>
                  </div>
                </>
              )}

              {/* Delete Confirmation Modal */}
              <dialog id="my_modal_5" className="modal modal-bottom sm:modal-middle">
                <div className="modal-box bg-gray-800 text-white">
                  <div className="flex flex-row items-center gap-3 mb-4">
                    <div className="flex size-12 shrink-0 items-center justify-center rounded-full bg-red-500/10">
                      <svg
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        strokeWidth="1.5"
                        className="size-6 text-red-400"
                      >
                        <path
                          d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126ZM12 15.75h.007v.008H12v-.008Z"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                        />
                      </svg>
                    </div>
                    <h3 className="font-bold text-lg text-red-400">Confirm Delete</h3>
                  </div>
                  <p className="py-4">Are you sure you want to delete this container?</p>
                  <div className="modal-action">
                    <button
                      className="btn btn-error"
                      onClick={() => {
                        if (selectedContainer) {
                          handleDelete(selectedContainer.containerId);
                        }
                        document.getElementById("my_modal_5").close();
                      }}
                    >
                      Confirm Delete
                    </button>
                    <form method="dialog">
                      <button className="btn btn-outline">Cancel</button>
                    </form>
                  </div>
                </div>
              </dialog>
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
                  className="text-gray-400 hover:text-white transition cursor-pointer"
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
                    value: `${selectedContainer.originCity}, ${selectedContainer.originCountry}`,
                  },
                  {
                    label: "Destination",
                    value: `${selectedContainer.destinationCity}, ${selectedContainer.destinationCountry}`,
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
              <div className="pt-5">
                <ProgressBar event={trackingEvent} />
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
                    className="text-cyan-400 hover:text-cyan-200 cursor-pointer"
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
                      className="w-full p-2 rounded bg-gray-900 border border-gray-700 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm mb-2">Origin</label>
                    <select
                      name="origin"
                      value={formData.origin}
                      onChange={handleChange}
                      className="bg-gray-900 w-full text-white border border-gray-700 rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                    <label className="block text-sm mb-2">Destination</label>
                    <select
                      name="destination"
                      value={formData.destination}
                      onChange={handleChange}
                      className="bg-gray-900 w-full text-white border border-gray-700 rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                    <label className="block text-sm mb-2">Weight</label>
                    <input
                      type="number"
                      name="weight"
                      value={formData.weight}
                      onChange={handleChange}
                      className="w-full p-2 rounded bg-gray-900 border border-gray-700 focus:border-cyan-400"
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
                      className="w-full p-2 rounded bg-gray-900 border border-gray-700 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm mb-2">Departure Date</label>
                    <input
                      type="datetime-local"
                      name="departureDate"
                      value={formData.departureDate}
                      onChange={handleChange}
                      className="w-full p-2 rounded bg-gray-900 border border-gray-700 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm mb-2">Arrival Date</label>
                    <input
                      type="datetime-local"
                      name="arrivalDate"
                      value={formData.arrivalDate}
                      onChange={handleChange}
                      className="w-full p-2 rounded bg-gray-900 border border-gray-700 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div className="col-span-2 flex justify-end">
                    <button
                      type="submit"
                      className="px-6 py-2 bg-green-600 hover:bg-green-500 rounded-lg shadow-md text-white font-medium cursor-pointer"
                    >
                      Save Container
                    </button>
                  </div>
                </form>
              </motion.div>
            </motion.div>
          )}
        </AnimatePresence>
        {/* UPDATE CONTAINER */}
        <AnimatePresence>
          {isUpdateModalOpen && (
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
                  <h2 className="text-2xl font-bold text-cyan-400">
                    Edit Container
                  </h2>
                  <button
                    onClick={() => setIsUpdateModalOpen(false)}
                    className="text-cyan-400 hover:text-cyan-200 cursor-pointer"
                  >
                    ✕
                  </button>
                </div>

                <form
                  onSubmit={handleUpdate}
                  className="grid grid-cols-2 gap-6"
                >
                  <div>
                    <label className="block text-sm mb-2">Container Type</label>
                    <input
                      type="text"
                      name="containerType"
                      value={formData.containerType}
                      onChange={handleUpdateChange}
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
                      onChange={handleUpdateChange}
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
                      onChange={handleUpdateChange}
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
                      onChange={handleUpdateChange}
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
                      onChange={handleUpdateChange}
                      className="w-full p-2 rounded bg-gray-800 border border-gray-600 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm mb-2">Departure Date</label>
                    <input
                      type="datetime-local"
                      name="departureDate"
                      value={formData.departureDate}
                      onChange={handleUpdateChange}
                      className="w-full p-2 rounded bg-gray-800 border border-gray-600 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm mb-2">Arrival Date</label>
                    <input
                      type="datetime-local"
                      name="arrivalDate"
                      value={formData.arrivalDate}
                      onChange={handleUpdateChange}
                      className="w-full p-2 rounded bg-gray-800 border border-gray-600 focus:border-cyan-400"
                      required
                    />
                  </div>
                  <div className="col-span-2 flex justify-end">
                    <button
                      type="submit"
                      className="px-6 py-2 bg-green-600 hover:bg-green-500 rounded-lg shadow-md text-white font-medium cursor-pointer"
                    >
                      Update Container
                    </button>
                  </div>
                </form>
              </motion.div>
            </motion.div>
          )}
        </AnimatePresence
        >
      </div>
    </div>
    </div>
  );
};

export default Dashboard;
