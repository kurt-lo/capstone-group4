import { Link } from 'react-router'
import Sidebar from './Sidebar'

const Home = () => {
  return (
    <div className='flex'>
      <Sidebar />
      <div className="text-white w-screen bg-[#0f1727]">
      <h1>Welcome Customer!</h1>
      <Link to='/containers'>View Containers</Link>
      </div>
    </div>
  )
}

export default Home