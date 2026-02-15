import "./areaChartBox.scss";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

const data = [
  {
    name: "Jan",
    food: 4000,
    flights: 1700,
    hotels: 2400,
  },
  {
    name: "Feb",
    food: 3000,
    flights: 2300,
    hotels: 2210,
  },
  {
    name: "Mar",
    food: 2000,
    flights: 1800,
    hotels: 2290,
  },
  {
    name: "Apr",
    food: 2780,
    flights: 1900,
    hotels: 2000,
  },
  {
    name: "May",
    food: 1890,
    flights: 1700,
    hotels: 2181,
  },
  {
    name: "Jun",
    food: 2390,
    flights: 1600,
    hotels: 2500,
  },
  {
    name: "Jul",
    food: 3490,
    flights: 1800,
    hotels: 2100,
  },
    {
    name: "Agust",
    food: 3490,
    flights: 1700,
    hotels: 2100,
  },
    {
    name: "Sep",
    food: 3490,
    flights: 1500,
    hotels: 2100,
  },
    {
    name: "Oct",
    food: 3490,
    flights: 1600,
    hotels: 2100,
  },
    {
    name: "Nov",
    food: 3490,
    flights: 1800,
    hotels: 2100,
  },
    {
    name: "Dec",
    food: 3490,
    flights: 2200,
    hotels: 2100,
  },
];

const AreaChartBox = () => {
  return (
    <div className="areaChartBox">
      <h1>Months spending Analytics</h1>
      <div className="chart">
        <ResponsiveContainer width="99%" height="100%">
          <AreaChart
            data={data}
            margin={{
              top: 10,
              right: 30,
              left: 0,
              bottom: 0,
            }}
          >
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Area
              type="monotone"
              dataKey="hotels"
              stackId="1"
              stroke="#8884d8"
              fill="#8884d8"
            />
            <Area
              type="monotone"
              dataKey="flights"
              stackId="1"
              stroke="#82ca9d"
              fill="#82ca9d"
            />
            <Area
              type="monotone"
              dataKey="food"
              stackId="1"
              stroke="#ffc658"
              fill="#ffc658"
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default AreaChartBox;
