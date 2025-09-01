import React, { useState } from "react";
import Sidebar from "../components/Sidebar";

const ReportsPage = () => {
    const [selected, setSelected] = useState("option1");

    return (
        <div className="flex">
            <Sidebar />
        <div id="Parent" className="w-screen flex text-white h-screen justify-center items-center bg-[#0f1727]">
            <div className="justify-center text-left mb-50 w-[80%]">
                <h1 className="text-4xl font-bold py-2">Find your booking</h1>
                <div id="radio-buttons" className="border-2 border-white h-fit rounded-xl p-2 space-x-2">
                    <input
                        type="radio"
                        id="option1"
                        name="report"
                        value="option1"
                        checked={selected === "option1"}
                        onChange={() => setSelected("option1")}
                        className="ml-4"
                    />
                    <label htmlFor="option1">Booking Number</label>
                    <input
                        type="radio"
                        id="option2"
                        name="report"
                        value="option2"
                        checked={selected === "option2"}
                        onChange={() => setSelected("option2")}
                    />
                    <label htmlFor="option2">BL Number</label>
                    <div className="flex justify-center">
                        <input
                            type="text"
                            placeholder={selected === "option1" ? "Booking Number" : "BL Number"}
                            className="border-1 border-white rounded-xl p-2 my-4 w-[98%]"
                        />
                    </div>
                </div>
            </div>
        </div>
        </div>
    );
};

export default ReportsPage;
