import React from 'react';
import { motion } from 'framer-motion';
import { Mail, Building, User } from 'lucide-react';
import { useState, useEffect } from 'react';
import useAuthStore from '../authentication/useAuthStore';
import axios from 'axios';
import { toast } from 'react-toastify';

const ProfileCard = () => {
    const [isEditing, setIsEditing] = useState(false);
    const [profileData, setProfileData] = useState({
        firstName: "Charles",
        lastName: "Leclerc",
        companyName: "Tech Innovations Inc.",
        emailAddress: "charles.leclerc@ferrari.com",
        userRole: "Regular User",
        bio: "Charles Marc Hervé Perceval Leclerc (French pronunciation: [ʃaʁl(ə) ləklɛʁ]; born 16 October 1997) is a Monégasque racing driver who competes in Formula One for Ferrari. Leclerc was runner-up in the Formula One World Drivers' Championship in 2022 with Ferrari, and has won eight Grands Prix across eight seasons.",
        avatar: "https://a.espncdn.com/combiner/i?img=/i/headshots/rpm/players/full/5498.png&w=350&h=254"
    });

    const token = useAuthStore((state) => state.user?.token);
    const username = useAuthStore((state) => state.user?.username);

    const fetchProfile = async () => {
        try {
            const fetchId = await axios.get(
                `http://localhost:9090/api/user/${username}`,
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );

            const data = fetchId.data;
            setProfileData(data);

        } catch (e) {
            console.error("Failed to fetch profile:", e);
            toast.error("Failed to load profile. Please check the backend.");
        } finally {
            console.log('Fetched profile data:', profileData);
            console.log('Fetched profile ID:', profileData.id);
            console.log('Avatar URL:', profileData.avatar); 
        }
    };

    const saveProfile = async () => {
    try {
      console.log('Current user:', useAuthStore.getState().user);
      console.log('Token:', token);
      console.log('Username:', username);
      console.log('Profile data ID:', profileData.id);
      console.log('Full profile data:', profileData);
      
      if (!token) {
        toast.error("No authentication token found. Please login again.");
        return;
      }
      
      if (!profileData.id) {
        toast.error("Profile ID not found. Please refresh the page.");
        return;
      }
      
      await axios.put(
        `http://localhost:9090/api/user/${profileData.id}`,
        profileData,
        {
          headers: { 
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        }
      );
      toast.success("Profile updated successfully");
    } catch (error) {
      console.error("Full error object:", error);
      console.error("Error response:", error.response);
      console.error("Error response data:", error.response?.data);
      
      if (error.response?.status === 403) {
        toast.error("Access denied. You can only update your own profile.");
      } else if (error.response?.status === 401) {
        toast.error("Authentication failed. Please login again.");
      } else {
        toast.error(`Failed to update profile: ${error.response?.data || error.message}`);
      }
    } finally {
      setIsEditing(false);
    }
  }

  useEffect(() => {
    fetchProfile();
  }, []);

  const handleEdit = () => {
    setIsEditing(!isEditing);
  };

  const handleSave = () => {
    saveProfile();
  };

  const handleCancel = () => {
    setIsEditing(false);
    fetchProfile();
  };

  const handleInputChange = (field, value) => {
    setProfileData(prev => ({
      ...prev,
      [field]: value
    }));
  };

    const containerVariants = {
        hidden: { opacity: 0, y: 20 },
        visible: {
            opacity: 1,
            y: 0,
            transition: {
                duration: 0.6,
                staggerChildren: 0.1
            }
        }
    };

    const itemVariants = {
        hidden: { opacity: 0, y: 10 },
        visible: { opacity: 1, y: 0 }
    };

    return (
        <motion.div
            variants={containerVariants}
            initial="hidden"
            animate="visible"
            className="bg-[#1c2433] rounded-3xl shadow-xl p-12 max-w-2xl w-full mx-auto border border-slate-100 mb-15"
        >
            {/* Profile Picture */}
            <motion.div variants={itemVariants} className="flex justify-center mb-8">
                <div className="relative">
                    <img
                        src={"https://a.espncdn.com/combiner/i?img=/i/headshots/rpm/players/full/5498.png&w=350&h=254"}
                        alt={`${profileData.firstName} ${profileData.lastName}`}
                        className="w-32 h-32 rounded-full object-cover border-4 border-violet-600 shadow-lg"
                        loading="lazy"
                        width="128"
                        height="128"
                    />
                    <div className="absolute -bottom-2 -right-2 w-8 h-8 bg-cyan-500 rounded-full border-4 border-white"></div>
                </div>
            </motion.div>

            {/* User Name */}
            <motion.div variants={itemVariants} className="text-center mb-6">
                <h1 className="text-4xl font-bold text-white font-inter mb-2">
                    {profileData.firstName} {profileData.lastName}
                </h1>
                <p className="text-xl text-violet-600 font-medium font-roboto">
                    {profileData.role}
                </p>
            </motion.div>

            {/* Bio Section */}
            <motion.div variants={itemVariants} className="text-center mb-10">
                <p className="text-gray-400 leading-relaxed font-roboto text-lg max-w-xl mx-auto">
                    Charles Marc Hervé Perceval Leclerc (French pronunciation: [ʃaʁl(ə) ləklɛʁ]; born 16 October 1997) is a Monégasque racing driver who competes in Formula One for Ferrari. Leclerc was runner-up in the Formula One World Drivers' Championship in 2022 with Ferrari, and has won eight Grands Prix across eight seasons.
                </p>
            </motion.div>

            {/* Details Section */}
            <motion.div variants={itemVariants} className="space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="flex items-center space-x-4 p-4 bg-[#0f1727] rounded-2xl hover:bg-[#32445f] transition-colors duration-200">
                        <div className="w-10 h-10 bg-violet-600 rounded-full flex items-center justify-center">
                            <User className="w-5 h-5 text-white" />
                        </div>
                        <div>
                            <p className="text-sm text-slate-400 font-medium font-inter uppercase tracking-wide">First Name</p>
                            <p className="text-white font-semibold font-roboto text-lg">{profileData.firstName}</p>
                        </div>
                    </div>

                    <div className="flex items-center space-x-4 p-4 bg-[#0f1727] rounded-2xl hover:bg-[#32445f] transition-colors duration-200">
                        <div className="w-10 h-10 bg-violet-600 rounded-full flex items-center justify-center">
                            <User className="w-5 h-5 text-white" />
                        </div>
                        <div>
                            <p className="text-sm text-slate-400 font-medium font-inter uppercase tracking-wide">Last Name</p>
                            <p className="text-white font-semibold font-roboto text-lg">{profileData.lastName}</p>
                        </div>
                    </div>

                    <div className="flex items-center space-x-4 p-4 bg-[#0f1727] rounded-2xl hover:bg-[#32445f] transition-colors duration-200">
                        <div className="w-10 h-10 bg-violet-600 rounded-full flex items-center justify-center">
                            <Building className="w-5 h-5 text-white" />
                        </div>
                        <div>
                            <p className="text-sm text-slate-400 font-medium font-inter uppercase tracking-wide">Company</p>
                            <p className="text-white font-semibold font-roboto text-lg">{profileData.companyName}</p>
                        </div>
                    </div>

                    <div className="flex items-center space-x-4 p-4 bg-[#0f1727] rounded-2xl hover:bg-[#32445f] transition-colors duration-200">
                        <div className="w-10 h-10 bg-violet-600 rounded-full flex items-center justify-center">
                            <Mail className="w-5 h-5 text-white" />
                        </div>
                        <div>
                            <p className="text-sm text-slate-400 font-medium font-inter uppercase tracking-wide">Email</p>
                            <p className="text-white font-semibold font-roboto text-md break-all">{profileData.emailAddress}</p>
                        </div>
                    </div>
                </div>
            </motion.div>
        </motion.div>
    );
};

export default ProfileCard;