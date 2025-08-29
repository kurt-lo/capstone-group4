import React, { useState } from 'react';

const Profile = () => {
  const [isEditing, setIsEditing] = useState(false);
  const [profileData, setProfileData] = useState({
    fullName: 'Cat on a pizza',
    department: 'Engineering',
    role: 'DPWH Contractor',
    experience: '5 Years',
    contactNumber: '09XXXXXXXX',
    location: 'Remote',
    email: 'CatOnAPizza@example.com',
    status: 'Active'
  });

  const handleEdit = () => {
    setIsEditing(!isEditing);
  };

  const handleSave = () => {
    setIsEditing(false);
    // TODO: Add API call to save profile data
    console.log('Saving profile data:', profileData);
  };

  const handleCancel = () => {
    setIsEditing(false);
    // TODO: Reset to original data if needed
  };

  const handleInputChange = (field, value) => {
    setProfileData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  return (
    <div className="h-full bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-black text-3xl font-bold pt-4 pl-10">Profile Page</h1>
          <p className="text-gray-400 text-lg pl-10">This is the profile page.</p>
        </div>
        <div className="pr-10 pt-4">
          {!isEditing ? (
            <button
              onClick={handleEdit}
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded flex items-center"
            >
              <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
              </svg>
              Edit Profile
            </button>
          ) : (
            <div className="space-x-2">
              <button
                onClick={handleSave}
                className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
              >
                Save
              </button>
              <button
                onClick={handleCancel}
                className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded"
              >
                Cancel
              </button>
            </div>
          )}
        </div>
      </div>
      <hr className="border-t border-gray-300 m-4" />
      <div className="flex flex-row m-4 ml-10 h-fit justify-center">
        <div className="w-100 h-100 bg-gray-700 flex-col rounded-3xl flex items-center justify-center text-white text-2xl font-bold m-10 bordercolor border-1 border-white p-4">
          <div className="text-3xl font-bold mb-4">
            <span>Pizza Cat</span>
          </div>
          <img
            src="https://i.pinimg.com/736x/84/b7/ff/84b7ff617adb7f43aae32f4ef0d1e9c1.jpg"
            alt="Profile"
            className="rounded-full w-150 h-150 m-10"
          />
        </div>
        <div className="w-250 h-150 bg-gray-700 rounded-3xl text-white text-xl font-bold m-10 border-1 border-white p-4">
          <div className="font-bold text-3xl m-4">
            <span>Bio & other details</span>
            <div className='flex-col flex mt-5'>
              <div className='flex flex-row justify-between text-base font-large mt-5'>
                <div className='flex flex-col'>
                  <span className='text-gray-400 text-lg'>Full Name</span>
                  {isEditing ? (
                    <input
                      type="text"
                      value={profileData.fullName}
                      onChange={(e) => handleInputChange('fullName', e.target.value)}
                      className="text-2xl font-normal bg-gray-600 text-white p-1 rounded"
                    />
                  ) : (
                    <p className='text-2xl font-normal'>{profileData.fullName}</p>
                  )}
                </div>
                <div className='flex flex-col text-right'>
                  <span className='text-gray-400 text-lg'>Department</span>
                  {isEditing ? (
                    <input
                      type="text"
                      value={profileData.department}
                      onChange={(e) => handleInputChange('department', e.target.value)}
                      className="text-2xl font-normal bg-gray-600 text-white p-1 rounded text-right"
                    />
                  ) : (
                    <p className='text-2xl font-normal'>{profileData.department}</p>
                  )}
                </div>
              </div>
              <hr className="border-t border-gray-300 my-2" />
              <div className='flex flex-row justify-between text-base font-large mt-10'>
                <div className='flex flex-col'>
                  <span className='text-gray-400 text-lg'>My Role</span>
                  {isEditing ? (
                    <input
                      type="text"
                      value={profileData.role}
                      onChange={(e) => handleInputChange('role', e.target.value)}
                      className="text-2xl font-normal bg-gray-600 text-white p-1 rounded"
                    />
                  ) : (
                    <p className='text-2xl font-normal'>{profileData.role}</p>
                  )}
                </div>
                <div className='flex flex-col text-right'>
                  <span className='text-gray-400 text-lg'>Experience</span>
                  {isEditing ? (
                    <input
                      type="text"
                      value={profileData.experience}
                      onChange={(e) => handleInputChange('experience', e.target.value)}
                      className="text-2xl font-normal bg-gray-600 text-white p-1 rounded text-right"
                    />
                  ) : (
                    <p className='text-2xl font-normal'>{profileData.experience}</p>
                  )}
                </div>
              </div>
              <hr className="border-t border-gray-300 my-2" />
              <div className='flex flex-row justify-between text-base font-large mt-10'>
                <div className='flex flex-col'>
                  <span className='text-gray-400 text-lg'>Contact Number</span>
                  {isEditing ? (
                    <input
                      type="text"
                      value={profileData.contactNumber}
                      onChange={(e) => handleInputChange('contactNumber', e.target.value)}
                      className="text-2xl font-normal bg-gray-600 text-white p-1 rounded"
                    />
                  ) : (
                    <p className='text-2xl font-normal'>{profileData.contactNumber}</p>
                  )}
                </div>
                <div className='flex flex-col text-right'>
                  <span className='text-gray-400 text-lg'>Location</span>
                  {isEditing ? (
                    <input
                      type="text"
                      value={profileData.location}
                      onChange={(e) => handleInputChange('location', e.target.value)}
                      className="text-2xl font-normal bg-gray-600 text-white p-1 rounded text-right"
                    />
                  ) : (
                    <p className='text-2xl font-normal'>{profileData.location}</p>
                  )}
                </div>
              </div>
              <hr className="border-t border-gray-300 my-2" />
              <div className='flex flex-row justify-between text-base font-large mt-10'>
                <div className='flex flex-col'>
                  <span className='text-gray-400 text-lg'>Email Address</span>
                  {isEditing ? (
                    <input
                      type="email"
                      value={profileData.email}
                      onChange={(e) => handleInputChange('email', e.target.value)}
                      className="text-2xl font-normal bg-gray-600 text-white p-1 rounded"
                    />
                  ) : (
                    <p className='text-2xl font-normal'>{profileData.email}</p>
                  )}
                </div>
                <div className='flex flex-col text-right'>
                  <span className='text-gray-400 text-lg'>Status</span>
                  {isEditing ? (
                    <select
                      value={profileData.status}
                      onChange={(e) => handleInputChange('status', e.target.value)}
                      className="text-2xl font-normal bg-gray-600 text-white p-1 rounded text-right"
                    >
                      <option value="Active">Active</option>
                      <option value="Inactive">Inactive</option>
                      <option value="Pending">Pending</option>
                    </select>
                  ) : (
                    <p className='text-2xl font-normal'>{profileData.status}</p>
                  )}
                </div>
              </div>
              <hr className="border-t border-gray-300 my-2" />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;

