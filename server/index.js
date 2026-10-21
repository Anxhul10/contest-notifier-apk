import express from "express";
import "dotenv/config";
import https from "node:https";
import { gql, request } from 'graphql-request'

const app = express();
const port = process.env.PORT;

function createTestContest(id, name, delaySeconds) {
    const startTimeSeconds = Math.floor(Date.now() / 1000) + delaySeconds;

    return {
        id,
        name,
        durationSeconds: 7200,
        startTimeSeconds,
        relativeTimeSeconds: -delaySeconds
    };
}

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
            // const now = Math.floor(Date.now() / 1000);
            
            // upcomingContests.result.push(
            //     createTestContest(-1, "Dummy Contest 10 min 20 sec", 620),
            //     createTestContest(-2, "Dummy Contest 1 hour 20 sec", 3620)
            // );
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
        contest.durationSeconds = contest.duration;
        contest.startTimeSeconds = contest.startTime;
        delete contest.duration;
        delete contest.startTime;
        contest["name"] = contest.title;
        upcomingContests.result.push(contest);
    }
    res.send(upcomingContests);
});

app.get('/api/codechef', (req, res) => {
    const currentTime = Math.floor(Date.now()/1000);

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
                contest.durationSeconds = contest.contest_duration*60;
                const startTime = Math.floor(
                    new Date(contest.contest_start_date_iso).getTime() / 1000
                );
                contest["relativeTimeSeconds"] = currentTime-startTime;
                delete contest.contest_duration;
                contest["name"] = contest.contest_name;
                upcomingContests.result.push(contest);
            }
            res.send(upcomingContests);
        })
    })
})

app.listen(port, "0.0.0.0", ()=> {
    console.log(`server started at http://localhost:${port}`);
});