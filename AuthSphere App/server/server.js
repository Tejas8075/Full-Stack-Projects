import express from 'express';
import cors from 'cors';
import 'dotenv/config'
import cookieParser from 'cookie-parser';

import connectDB from './config/mongodb.js';
import authRouter from './routes/authRoutes.js'
import userRouter from './routes/userRoutes.js';

const app = express();

// Port number
// if we define the variable in the env variable then it will be used otherwise 4000 will be used
const port = process.env.PORT || 4000
connectDB();

// variable for allowing frontend to access backend
const allowedOrigins = ['http://localhost:5173'];

app.use(express.json());
app.use(cookieParser());
// To send the cookie from the express app
app.use(cors({origin: allowedOrigins, credentials: true}));

// API Endpoints
app.get("/", (req, res) => res.send("API Working"));

// Actual endpoints will be /api/auth/login, and all the endpoints in the Routes
// For auth
app.use("/api/auth", authRouter)

// For user data
app.use("/api/user", userRouter);

app.listen(port, () => console.log(`Server started on PORT ${port}`));
