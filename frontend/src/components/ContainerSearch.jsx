import { useState } from 'react';

const ContainerSearch = () => {
  const [filters, setFilters] = useState({
    containerType: '',
    owner: '',
    origin: '',
    destination: '',
  });

  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setResults([]);

    const queryParams = new URLSearchParams(
      Object.entries(filters).filter(([_, val]) => val.trim() !== '')
    );

    try {
      const res = await fetch(`http://localhost:9090/api/containers/search?${queryParams}`);
      if (!res.ok) throw new Error('Fetch failed');
      const data = await res.json();
      setResults(data);
    } catch (err) {
      setError('Error fetching containers');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-6">
      <h2 className="text-2xl font-bold mb-4">Container Search</h2>

      <form onSubmit={handleSubmit} className="flex flex-wrap gap-4 mb-6">
        <input
          type="text"
          name="containerType"
          placeholder="Container Type"
          className="input input-bordered w-full md:w-48"
          value={filters.containerType}
          onChange={handleChange}
        />
        <input
          type="text"
          name="owner"
          placeholder="Owner"
          className="input input-bordered w-full md:w-48"
          value={filters.owner}
          onChange={handleChange}
        />
        <input
          type="text"
          name="origin"
          placeholder="Origin"
          className="input input-bordered w-full md:w-48"
          value={filters.origin}
          onChange={handleChange}
        />
        <input
          type="text"
          name="destination"
          placeholder="Destination"
          className="input input-bordered w-full md:w-48"
          value={filters.destination}
          onChange={handleChange}
        />
        <button
          type="submit"
          className="btn btn-primary"
          disabled={loading}
        >
          {loading ? 'Searching...' : 'Search'}
        </button>
      </form>

      {error && <p className="text-red-500 mb-4">{error}</p>}

      {!loading && results.length > 0 && (
        <div className="overflow-x-auto">
          <table className="table table-zebra w-full">
            <thead>
              <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Owner</th>
                <th>Origin</th>
                <th>Destination</th>
              </tr>
            </thead>
            <tbody>
              {results.map((c) => (
                <tr key={c.id}>
                  <td>{c.id}</td>
                  <td>{c.containerType}</td>
                  <td>{c.owner}</td>
                  <td>{c.origin}</td>
                  <td>{c.destination}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {!loading && results.length === 0 && !error && (
        <p className="text-gray-600">No data found.</p>
      )}
    </div>
  );
};

export default ContainerSearch;
