import express from "express";

const app = express();

app.get("/", (req, res) => {

    res.send({"status": 200});
});

app.listen(3000);