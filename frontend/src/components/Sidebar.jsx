import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Sidebar = () => {
  const [isHovered, setIsHovered] = useState(false);
  const navigate = useNavigate();

  const menuItems = [
    {
      name: 'Home',
      icon: (
        <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
        </svg>
      ),
      href: '/'
    },
    {
      name: 'Dashboard',
      icon: (
        <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
        </svg>
      ),
      href: '/dashboard'
    },
    {
      name: 'Reports',
      icon: (
        <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
        </svg>
      ),
      href: '/reports'
    },
    {
        name: 'Profile',
        icon: (
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5.121 17.804A13.937 13.937 0 0112 15c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0zm6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        ),
        href: '/profile'
    }
  ];

  const handleNavigation = (href) => {
    navigate(href);
  };

  return (
    <div 
      className={`bg-[#1e2939] h-screen transition-all duration-300 ease-in-out ${
        isHovered ? 'w-64' : 'w-16'
      } flex flex-col shadow-lg`}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <div className="flex items-center justify-center p-4 border-b border-base-300">
        {isHovered ? (
          <h2 className="text-xl font-bold text-base-content">Menu</h2>
        ) : (
          <div className="w-8 h-8 bg-[#6366f1] rounded-lg flex items-center justify-center">
            <svg className="w-5 h-5 text-primary-content" fill="currentColor" viewBox="0 0 24 24">
              <path d="M3 6h18v2H3V6m0 5h18v2H3v-2m0 5h18v2H3v-2Z"/>
            </svg>
          </div>
        )}
      </div>

      <nav className="flex-1 px-2 py-4 space-y-2">
        {menuItems.map((item, index) => (
          <button
            key={index}
            onClick={() => handleNavigation(item.href)}
            className={`flex items-center w-full p-3 rounded-lg hover:bg-base-300 transition-colors duration-200 group ${
              !isHovered ? 'justify-center' : ''
            }`}
            title={!isHovered ? item.name : ''}
          >
            <span className="text-white group-hover:text-primary">
              {item.icon}
            </span>
            {isHovered && (
              <span className="ml-3 text-white group-hover:text-primary font-medium">
                {item.name}
              </span>
            )}
          </button>
        ))}
      </nav>
    </div>
  );
};

export default Sidebar;