// import React, { useEffect, useState } from 'react';
// import Sidebar from '../components/Sidebar';
// import useAuthStore from "../authentication/useAuthStore";
// import { toast } from 'react-toastify';
// import axios from 'axios';

// const Profile = () => {
//   const [isEditing, setIsEditing] = useState(false);
//   const [profileData, setProfileData] = useState({
//     firstName: '',
//     lastName: '',
//     companyName: '',
//     email: '',
//     userRole: ''
//   });

//   const token = useAuthStore((state) => state.user?.token);
//   const username = useAuthStore((state) => state.user?.username);

//   const fetchProfile = async () => {
//     try {
//       const fetchId = await axios.get(
//         `http://localhost:9090/api/user/${username}`,
//         {
//           headers: { Authorization: `Bearer ${token}` },
//         }
//       );

//       const data = fetchId.data;
//       setProfileData(data);

//     } catch (e) {
//       console.error("Failed to fetch profile:", e);
//       toast.error("Failed to load profile. Please check the backend.");
//     } finally {
//       console.log('Fetched profile data:', profileData);
//       console.log('Fetched profile ID:', profileData.id);
//     }
//   };

//   const saveProfile = async () => {
//     try {
//       console.log('Current user:', useAuthStore.getState().user);
//       console.log('Token:', token);
//       console.log('Username:', username);
//       console.log('Profile data ID:', profileData.id);
//       console.log('Full profile data:', profileData);
      
//       if (!token) {
//         toast.error("No authentication token found. Please login again.");
//         return;
//       }
      
//       if (!profileData.id) {
//         toast.error("Profile ID not found. Please refresh the page.");
//         return;
//       }
      
//       await axios.put(
//         `http://localhost:9090/api/user/${profileData.id}`,
//         profileData,
//         {
//           headers: { 
//             Authorization: `Bearer ${token}`,
//             'Content-Type': 'application/json'
//           },
//         }
//       );
//       toast.success("Profile updated successfully");
//     } catch (error) {
//       console.error("Full error object:", error);
//       console.error("Error response:", error.response);
//       console.error("Error response data:", error.response?.data);
      
//       if (error.response?.status === 403) {
//         toast.error("Access denied. You can only update your own profile.");
//       } else if (error.response?.status === 401) {
//         toast.error("Authentication failed. Please login again.");
//       } else {
//         toast.error(`Failed to update profile: ${error.response?.data || error.message}`);
//       }
//     } finally {
//       setIsEditing(false);
//     }
//   }

//   useEffect(() => {
//     fetchProfile();
//   }, []);

//   const handleEdit = () => {
//     setIsEditing(!isEditing);
//   };

//   const handleSave = () => {
//     saveProfile();
//   };

//   const handleCancel = () => {
//     setIsEditing(false);
//     fetchProfile();
//   };

//   const handleInputChange = (field, value) => {
//     setProfileData(prev => ({
//       ...prev,
//       [field]: value
//     }));
//   };

//   return (
//     <div className='h-screen w-auto bg-[#0f1727] text-white flex'>
//       <Sidebar />
//       <div className="justify-center items-center w-full">
//         <div>
//           <div>
//             <h1 className="text-white text-3xl font-bold pt-4 pl-10">Profile Page</h1>
//             <p className="text-gray-400 text-lg pl-10">This is the profile page.</p>
//           </div>

//         </div>
//         <hr className="border-gray-600 m-4 border-dashed mx-auto w-[95%]" />
//         <div className="flex flex-row w-300 mx-auto justify-center">
//           <div id='Profile Pic' className="w-100 h-100 bg-[#0f1727] flex-col rounded-3xl flex items-center justify-center text-white text-2xl font-bold m-10 border-1 border-gray-600 p-4">
//             <div className="w-auto text-3xl font-bold">
//               <span>{profileData.firstName} {profileData.lastName}</span>
//             </div>
//             <div className='text-[#a6ce88] text-sm flex justify-center text-center'>
//               <span>Regular User</span>
//             </div>
//             <img
//               src="https://i.pinimg.com/736x/84/b7/ff/84b7ff617adb7f43aae32f4ef0d1e9c1.jpg"
//               alt="Profile"
//               className="rounded-full w-150 h-150 m-10"
//             />
//           </div>
//           <div
//             id='Bio & other details'
//             className="max-w-xl w-full bg-[#0f1727] rounded-3xl text-white text-xl font-bold m-10 border-1 border-gray-600 p-4 break-words"
//           >
//             <div className="flex justify-between font-bold text-xl m-4 mb-6">
//               <span>Bio & other details</span>
//             </div>
//             <div className='flex-col flex ml-8'>
//               <div className='flex flex-row justify-between text-base font-large'>
//                 <div className='flex flex-col'>
//                   <span className='text-gray-400 text-lg'>First Name</span>
//                   {isEditing ? (
//                     <input
//                       type="text"
//                       value={profileData.firstName}
//                       onChange={(e) => handleInputChange('firstName', e.target.value)}
//                       className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
//                     />
//                   ) : (
//                     <p className='text-sm font-semibold break-words'>{profileData.firstName}</p>
//                   )}
//                 </div>
//                 <div className='flex flex-col text-right'>
//                   <span className='text-gray-400 text-lg'>Last Name</span>
//                   {isEditing ? (
//                     <input
//                       type="text"
//                       value={profileData.lastName}
//                       onChange={(e) => handleInputChange('lastName', e.target.value)}
//                       className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
//                     />
//                   ) : (
//                     <p className='text-sm font-semibold break-words'>{profileData.lastName}</p>
//                   )}
//                 </div>
//               </div>

//               <hr className="border-t border-gray-600 my-2" />
//               <div className='flex flex-row justify-between text-base font-large'>
//                 <div className='flex flex-col'>
//                   <span className='text-gray-400 text-lg break-words'>Company</span>
//                   {isEditing ? (
//                     <input
//                       type="text"
//                       value={profileData.companyName}
//                       onChange={(e) => handleInputChange('companyName', e.target.value)}
//                       className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
//                     />
//                   ) : (
//                     <p className='text-sm font-semibold break-words'>{profileData.companyName}</p>
//                   )}
//                 </div>
//                 <div className='flex flex-col text-right'>
//                   <span className='text-gray-400 text-lg break-words'>Email Address</span>
//                   {isEditing ? (
//                     <input
//                       type="text"
//                       value={profileData.emailAddress}
//                       onChange={(e) => handleInputChange('emailAddress', e.target.value)}
//                       className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
//                     />
//                   ) : (
//                     <p className='text-sm font-semibold break-words'>{profileData.emailAddress}</p>
//                   )}
//                 </div>
//               </div>
//               <hr className="border-t border-gray-600 my-2" />
//               <div className='flex flex-row justify-between text-base font-large'>
//                 <div className='flex flex-col'>
//                   <span className='text-gray-400 text-lg'>User Role</span>
//                   {isEditing ? (
//                     <input
//                       type="text"
//                       value={profileData.role}
//                       onChange={(e) => handleInputChange('role', e.target.value)}
//                       className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
//                     />
//                   ) : (
//                     <p className='text-sm font-semibold break-words'>{profileData.role}</p>
//                   )}
//                 </div>

//               </div>
//               <hr className="border-t border-gray-600 my-2" />
//               <div id="edit-button" className="flex justify-end mt-2">
//                 {!isEditing ? (
//                   <button
//                     onClick={handleEdit}
//                     className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded flex items-center text-sm"
//                   >
//                     <svg className="w-3 h-3 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
//                       <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
//                     </svg>
//                     Edit Profile
//                   </button>
//                 ) : (
//                   <div className="space-x-2">
//                     <button
//                       onClick={handleSave}
//                       className="bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-2 rounded text-sm"
//                     >
//                       Save
//                     </button>
//                     <button
//                       onClick={handleCancel}
//                       className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-1 px-2 rounded text-sm"
//                     >
//                       Cancel
//                     </button>
//                   </div>
//                 )}
//               </div>
//             </div>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default Profile;

import Sidebar from "../components/Sidebar";
import ProfileCard from "../components/ProfileCard"

const Profile = () => {

  return (
  <div className="flex bg-[#0f1727] text-white items-center">
    <Sidebar />
    <div className="flex-grow justify-center">
      <ProfileCard />
    </div>
  </div>
  )
}

export default Profile;
