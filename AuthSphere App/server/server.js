import express from 'express';
import cors from 'cors';
import 'dotenv/config'
import cookieParser from 'cookie-parser';

import connectDB from './config/mongodb.js';

const app = express();

// Port number
// if we define the variable in the env variable then it will be used otherwise 4000 will be used
const port = process.env.PORT || 4000
connectDB();

app.use(express.json());
app.use(cookieParser());
// To send the cookie from the express app
app.use(cors({credentials: true}));

app.get("/", (req, res) => res.send("API Working"));

app.listen(port, () => console.log(`Server started on PORT ${port}`));
