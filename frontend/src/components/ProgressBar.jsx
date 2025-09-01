import React, { useState } from 'react';
import { FaCheck, FaUser, FaLock, FaCheckCircle } from 'react-icons/fa';
import { motion } from 'framer-motion';

const MultiStepProgressBar = () => {
    const [currentStep, setCurrentStep] = useState(1);
    const steps = [
        { name: 'Packing', icon: <FaUser /> },
        { name: 'In Transit', icon: <FaLock /> },
        { name: 'Delivered', icon: <FaLock /> },
        { name: 'Completed', icon: <FaLock /> },
        { name: 'Reviewed', icon: <FaLock /> }
    ];

    const stepDetails = [
        "Packing Date...",
        "In Transit Date...",
        "Delivered Date...",
        "Completed Date...",
    ];

    const handleStepClick = (step) => {
        setCurrentStep(step);
    };

    return (
        <div className="flex justify-center">
            <div className="w-full p-4 rounded-lg shadow-lg">
                <div className="flex flex-col items-left space-y-2">
                    {steps.map((step, index) => (
                        <React.Fragment key={index}>
                            <motion.div
                                className={`relative flex items-center justify-center w-5 h-5 rounded-full cursor-pointer ${index + 1 <= currentStep ? 'bg-green-500' : 'bg-gray-300'
                                    }`}
                                whileHover={{ scale: 1.1 }}
                                whileTap={{ scale: 0.9 }}
                                onClick={() => handleStepClick(index + 1)}
                                role="button"
                                tabIndex={0}
                                aria-label={`Step ${index + 1}: ${step.name}`}
                            >
                                <span className="text-white text-xs">
                                    {index + 1 <= currentStep ? <FaCheck size={10} /> : step.icon}
                                </span>
                                <div className="absolute left-full ml-2 whitespace-nowrap text-sm font-medium">
                                    {step.name}
                                </div>
                            </motion.div>
                            {index < steps.length - 1 && (
                                <div className="relative">
                                    <motion.div
                                        className={`h-16 w-1 ml-2 ${index + 1 < currentStep ? 'bg-green-500' : 'bg-gray-300'
                                            }`}
                                        initial={{ height: 0 }}
                                        animate={{ height: '3rem' }}
                                        transition={{ duration: 0.5 }}
                                    />
                                    <div className="absolute top-1/2 left-6 transform -translate-y-1/2 text-xs text-gray-500 italic">
                                        {stepDetails[index]}
                                    </div>
                                </div>
                            )}
                        </React.Fragment>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default MultiStepProgressBar;