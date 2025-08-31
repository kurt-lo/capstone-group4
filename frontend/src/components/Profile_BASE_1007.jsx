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
    <div className="h-screen w-auto bg-[#1f2024] text-white">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-white text-3xl font-bold pt-4 pl-10">Profile Page</h1>
          <p className="text-gray-400 text-lg pl-10">This is the profile page.</p>
        </div>

      </div>
      <hr className="border-gray-600 m-4 border-dashed mx-auto w-[95%]" />
      <div className="flex flex-row h-fit w-[80%] mx-auto justify-center">
        <div id='Profile Pic' className="w-100 h-100 bg-[#1f2024] flex-col rounded-3xl flex items-center justify-center text-white text-2xl font-bold m-10 border-1 border-gray-600 p-4">
          <div className="w-auto text-3xl font-bold">
            <span>Pizza Cat</span>
          </div>
          <div className='text-[#a6ce88] text-sm flex justify-center text-center'>
            <span>Regular User</span>
          </div>
          <img
            src="https://i.pinimg.com/736x/84/b7/ff/84b7ff617adb7f43aae32f4ef0d1e9c1.jpg"
            alt="Profile"
            className="rounded-full w-150 h-150 m-10"
          />
        </div>
        <div
          id='Bio & other details'
          className="max-w-xl w-full bg-[#1f2024] rounded-3xl text-white text-xl font-bold m-10 border-1 border-gray-600 p-4 break-words"
        >
          <div className="flex justify-between font-bold text-xl m-4 mb-6">
            <span>Bio & other details</span>
            
          </div>

          <div className='flex-col flex ml-8'>
            <div className='flex flex-row justify-between text-base font-large'>
              <div className='flex flex-col'>
                <span className='text-gray-400 text-lg'>Full Name</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={profileData.fullName}
                    onChange={(e) => handleInputChange('fullName', e.target.value)}
                    className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
                  />
                ) : (
                  <p className='text-sm font-semibold break-words'>{profileData.fullName}</p>
                )}
              </div>
              <div className='flex flex-col text-right'>
                <span className='text-gray-400 text-lg'>Department</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={profileData.department}
                    onChange={(e) => handleInputChange('department', e.target.value)}
                    className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
                  />
                ) : (
                  <p className='text-sm font-semibold break-words'>{profileData.department}</p>
                )}
              </div>
            </div>
            <hr className="w-full border-t border-gray-600 my-2" />
            <div className='flex flex-row justify-between text-base font-large'>
              <div className='flex flex-col'>
                <span className='text-gray-400 text-lg'>My Role</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={profileData.role}
                    onChange={(e) => handleInputChange('role', e.target.value)}
                    className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
                  />
                ) : (
                  <p className='text-sm font-semibold break-words'>{profileData.role}</p>
                )}
              </div>
              <div className='flex flex-col text-right'>
                <span className='text-lg font-semibold break-words text-gray-400'>Experience</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={profileData.experience}
                    onChange={(e) => handleInputChange('experience', e.target.value)}
                    className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
                  />
                ) : (
                  <p className='text-sm font-semibold break-words'>{profileData.experience}</p>
                )}
              </div>
            </div>
            <hr className="border-t border-gray-600 my-2" />
            <div className='flex flex-row justify-between text-base font-large'>
              <div className='flex flex-col'>
                <span className='text-gray-400 text-lg break-words'>Contact Number</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={profileData.contactNumber}
                    onChange={(e) => handleInputChange('contactNumber', e.target.value)}
                    className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
                  />
                ) : (
                  <p className='text-sm font-semibold break-words'>{profileData.contactNumber}</p>
                )}
              </div>
              <div className='flex flex-col text-right'>
                <span className='text-gray-400 text-lg break-words'>Location</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={profileData.location}
                    onChange={(e) => handleInputChange('location', e.target.value)}
                    className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
                  />
                ) : (
                  <p className='text-sm font-semibold break-words'>{profileData.location}</p>
                )}
              </div>
            </div>
            <hr className="border-t border-gray-600 my-2" />
            <div className='flex flex-row justify-between text-base font-large'>
              <div className='flex flex-col'>
                <span className='text-gray-400 text-lg'>Email Address</span>
                {isEditing ? (
                  <input
                    type="email"
                    value={profileData.email}
                    onChange={(e) => handleInputChange('email', e.target.value)}
                    className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
                  />
                ) : (
                  <p className='text-sm font-semibold break-words'>{profileData.email}</p>
                )}
              </div>
              <div className='flex flex-col text-right'>
                <span className='text-gray-400 text-lg'>Status</span>
                {isEditing ? (
                  <select
                    value={profileData.status}
                    onChange={(e) => handleInputChange('status', e.target.value)}
                    className="font-normal bg-gray-600 text-md text-white rounded max-w-full truncate"
                  >
                    <option value="Active">Active</option>
                    <option value="Inactive">Inactive</option>
                    <option value="Pending">Pending</option>
                  </select>
                ) : (
                  <p className='text-sm font-semibold break-words'>{profileData.status}</p>
                )}
              </div>
            </div>
            <hr className="border-t border-gray-600 my-2" />
            <div id="edit-button" className="flex justify-end mt-2">
              {!isEditing ? (
                <button
                  onClick={handleEdit}
                  className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded flex items-center text-sm"
                >
                  <svg className="w-3 h-3 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                  Edit Profile
                </button>
              ) : (
                <div className="space-x-2">
                  <button
                    onClick={handleSave}
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-2 rounded text-sm"
                  >
                    Save
                  </button>
                  <button
                    onClick={handleCancel}
                    className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-1 px-2 rounded text-sm"
                  >
                    Cancel
                  </button>
                </div>
              )}
            </div>
          </div>

        </div>
      </div>
      <div id='social media' className='w-[55%] mx-auto border-gray-600 border font-normal text-white text-center mt-4 rounded-lg bg-[#1f2024]'>
        <div className='text-md font-bold mt-2 mb-4 flex ml-4'>Socials</div>
        <div className='flex flex-row  ml-10 space-x-10 mb-4 '>
          <img src='https://upload.wikimedia.org/wikipedia/commons/5/51/Facebook_f_logo_%282019%29.svg' alt='Facebook' className='w-8 h-8' />
          <img src='https://upload.wikimedia.org/wikipedia/commons/1/19/Spotify_logo_without_text.svg' alt='Spotify' className='w-8 h-8' />
          <img src='https://upload.wikimedia.org/wikipedia/commons/a/a5/Instagram_icon.png' alt='Instagram' className='w-8 h-8' />
        </div>
      </div>
    </div>
  );
};

export default Profile;

