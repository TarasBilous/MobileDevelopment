const { DataTypes } = require("sequelize");
const { sequelize } = require("../db");

const Panel = sequelize.define("solar_station", {
  id: {
    type: DataTypes.INTEGER,
    autoIncrement: true,
    primaryKey: true
  },
  panel_type: {
    type: DataTypes.STRING
  },
  power: {
    type: DataTypes.INTEGER
  },
  battery_capacity: {
    type: DataTypes.INTEGER
  },
  live_duration: {
    type: DataTypes.INTEGER
  },
  address: {
    type: DataTypes.STRING
  },
  image: {
    type: DataTypes.TEXT
  }
});

module.exports = Panel;
