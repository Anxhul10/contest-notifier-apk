import express from "express";
import "dotenv/config";
import https from "node:https";

const app = express();
const port = process.env.PORT;

app.get("/health", (req, res) => {

    res.send({"status": "Ok"});
});

app.get("/api/codeforces", (req, res) => {
    let upcomingContests = {
        status: "Ok",
        result: []
    }

    https.get("https://codeforces.com/api/contest.list", (apiRes) =>{
        
        let body = "";

        apiRes.on("data", chunk => body+=chunk);

        apiRes.on("end", () => {
            const data = JSON.parse(body);
            for(const contest of data.result) {
                if(contest.phase === "BEFORE") {
                    console.log(contest);
                    upcomingContests.result.push(contest);
                    console.log(upcomingContests.result.length)
                }
            }
            res.send(upcomingContests);
        })
    })
})

app.listen(port, ()=> {
    console.log(`server started at http://localhost:${port}`);
});