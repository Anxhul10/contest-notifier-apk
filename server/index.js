import express from "express";
import "dotenv/config";

const app = express();
const port = process.env.PORT;

app.get("/", (req, res) => {

    res.send({"status": 200});
});

app.listen(port, ()=> {
    console.log(`server started at http://localhost:${port}`);
});