
import express from "express";

const app = express();
const port = 3000;

function createTestContest(id, name) {
    // Contest starts in 10 minutes and 20 seconds
    const startTimeSeconds = Math.floor(Date.now() / 1000) + 620;

    return {
        id,
        name,
        durationSeconds: 7200,
        startTimeSeconds,
        relativeTimeSeconds: -620
    };
}

app.get("/health", (req, res) => {
    res.json({ status: "Ok" });
});

app.get("/api/codeforces", (req, res) => {
    res.json({
        status: "Ok",
        result: [
            {
                ...createTestContest(1, "Test Codeforces Contest"),
                type: "CF",
                phase: "BEFORE",
                frozen: false
            }
        ]
    });
});

app.get("/api/leetcode", (req, res) => {
    res.json({
        status: "Ok",
        result: [
            createTestContest(2, "Test LeetCode Contest")
        ]
    });
});

app.get("/api/codechef", (req, res) => {
    res.json({
        status: "Ok",
        result: [
            createTestContest(3, "Test CodeChef Contest")
        ]
    });
});

app.listen(port, "0.0.0.0", () => {
    console.log(`Test server running on port ${port}`);
});
