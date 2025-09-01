import React, { useState } from "react";
import { FaCheck, FaMapMarkerAlt } from "react-icons/fa";
import { motion } from "framer-motion";

const MultiStepProgressBar = ({ event }) => {
  const [currentStep, setCurrentStep] = useState(1);

  const handleStepClick = (step) => {
    setCurrentStep(step);
  };

  return (
    <div className="flex justify-center">
      <div className="w-full p-4 rounded-lg shadow-lg">
        <div className="flex flex-col items-left space-y-2">
          {event.map((step, index) => (
            <React.Fragment key={step.eventId}>
              <motion.div
                className={`relative flex items-center justify-center w-5 h-5 rounded-full cursor-pointer bg-green-500`}
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.9 }}
                onClick={() => handleStepClick(index + 1)}
                role="button"
                tabIndex={0}
                aria-label={`Step ${index + 1}: ${step.eventType}`}
              >
                <span className="text-white text-xs">
                  {index + 1 <= currentStep ? (
                    <FaCheck size={10} />
                  ) : (
                    <FaMapMarkerAlt size={10} />
                  )}
                </span>

                {/* Label for the step */}
                <div className="absolute left-full ml-2 whitespace-nowrap text-sm font-medium">
                  {step.eventType}
                  <div className="text-xs text-gray-500 italic">
                    {step.eventDate
                      ? new Date(step.eventDate).toLocaleString()
                      : "Pending..."}
                    <br />
                    {step.locationName} → {step.nextLocationName}
                  </div>
                </div>
              </motion.div>

              {/* Connector line */}
              {index < event.length - 1 && (
                <motion.div
                  className="h-16 w-1 ml-2 bg-green-500"
                  initial={{ height: 0 }}
                  animate={{ height: "3rem" }}
                  transition={{ duration: 0.5 }}
                />
              )}
            </React.Fragment>
          ))}
        </div>
      </div>
    </div>
  );
};

export default MultiStepProgressBar;
