import express from "express";
import "dotenv/config";
import https from "node:https";
import { gql, request } from 'graphql-request'

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
                    upcomingContests.result.push(contest);
                }
            }
            res.send(upcomingContests);
        })
    })
})


app.get('/api/leetcode', async (req, res) => {
    let upcomingContests = {
        status: "Ok",
        result: []
    }
    const currentTime = Math.floor(Date.now()/1000);

    const query = `
        {
            contestV2UpcomingContests {
                title
                startTime
                duration
            }
        }
    `
    const contests = await request('https://leetcode.com/graphql/', query);
    
    for(const contest of contests.contestV2UpcomingContests) {
        contest["relativeTimeSeconds"] = currentTime-contest["startTime"];
        upcomingContests.result.push(contest);
    }
    res.send(upcomingContests);
});

app.get('/api/codechef', (req, res) => {
    let upcomingContests = {
        status: "Ok",
        result: []
    }

    https.get("https://www.codechef.com/api/list/contests/all?sort_by=START&sorting_order=asc&offset=0&mode=all", (apiRes) => {
        
        let body = "";

        apiRes.on("data", chunk => body+=chunk);

        apiRes.on("end", () => {
            const data = JSON.parse(body);
            for(const contest of data.future_contests) {
                upcomingContests.result.push(contest);
            }
            res.send(upcomingContests);
        })
    })
})

app.listen(port, ()=> {
    console.log(`server started at http://localhost:${port}`);
});