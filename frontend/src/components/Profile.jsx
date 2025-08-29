import React from 'react';

const Profile = () => {
  return (
    <div>
      <h1 className="text-4xl font-bold pt-4 pl-10">Profile Page</h1>
      <p className="text-gray-600 text-lg pl-10">This is the profile page.</p>
      <hr className="border-t border-gray-300 m-4" />
      <div className="flex flex-row m-4 ml-10 h-250 justify-center">
        <div className="w-200 h-200 bg-gray-700 flex-col rounded-4xl flex items-center justify-center text-white text-2xl font-bold m-10 bordercolor border-1 border-white p-4">
          <div className="text-4xl font-bold mb-4">
          <span>Pizza Cat</span>
          </div>
          <img
            src="https://i.pinimg.com/736x/84/b7/ff/84b7ff617adb7f43aae32f4ef0d1e9c1.jpg"
            alt="Profile"
            className="rounded-full w-150 h-150 m-10"
          />
        </div>
        <div className="w-300 h-200 bg-gray-700 rounded-4xl text-white text-xl font-bold m-10 border-1 border-white p-4">
          <div className="font-bold text-4xl mb-4">
            <span>Bio & other details</span>
            <div className=''>
              <div className='flex flex-row justify-between text-base font-large mt-20'>
                <div className='flex flex-col'>
                  <span className='text-gray-400 text-lg'>Full Name</span>
                  <p className='text-sm font-normal'>Cat on a pizza</p>
                </div>
                <div className='flex flex-col text-right'>
                  <span className='text-gray-400 text-lg'>Department</span>
                  <p className='text-sm font-normal'>Engineering</p>
                </div>
              </div>
              <hr className="border-t border-gray-300 my-2" />
              <div className='flex flex-row justify-between text-base font-large mt-15'>
                <div className='flex flex-col'>
                  <span className='text-gray-400 text-lg'>My Role</span>
                  <p className='text-sm font-normal'>DPWH Contractor</p>
                </div>
                <div className='flex flex-col text-right'>
                  <span className='text-gray-400 text-lg'>Experience</span>
                  <p className='text-sm font-normal'>5 Years</p>
                </div>
              </div>
              <hr className="border-t border-gray-300 my-2" />
              <div className='flex flex-row justify-between text-base font-large mt-15'>
                <div className='flex flex-col'>
                  <span className='text-gray-400 text-lg'>Contact Number</span>
                  <p className='text-sm font-normal'>09XXXXXXXX</p>
                </div>
                <div className='flex flex-col text-right'>
                  <span className='text-gray-400 text-lg'>Location</span>
                  <p className='text-sm font-normal'>Remote</p>
                </div>
              </div>
              <hr className="border-t border-gray-300 my-2" />
              <div className='flex flex-row justify-between text-base font-large mt-15'>
                <div className='flex flex-col'>
                  <span className='text-gray-400 text-lg'>Email Address</span>
                  <p className='text-sm font-normal'>CatOnAPizza@example.com</p>
                </div>
                <div className='flex flex-col text-right'>
                  <span className='text-gray-400 text-lg'>Status</span>
                  <p className='text-sm font-normal'>Active</p>
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

