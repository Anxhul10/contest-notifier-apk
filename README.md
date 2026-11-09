# Contest Notifier

Contest Notifier is an Android app that shows upcoming coding contests from different platforms in one place and sends reminders before they start.

## Screenshots

<table>
  <tr>
    <td align="center">
      <strong>Notifications</strong><br>
      <img src="https://github.com/user-attachments/assets/52a95a1a-0dbd-4400-a140-30441ba6aae8" width="220">
    </td>
    <td align="center">
      <strong>Light Mode</strong><br>
      <img src="https://github.com/user-attachments/assets/3e0f0e19-819b-488e-83c9-880b15ddbdf4" width="220">
    </td>
    <td align="center">
      <strong>Dark Mode</strong><br>
      <img src="https://github.com/user-attachments/assets/0c40c124-a8c0-4c22-b736-b67352ff5f6a" width="220">
    </td>
    <td align="center">
      <strong>Loading</strong><br>
      <img src="https://github.com/user-attachments/assets/d49d336b-e62e-4481-ac9d-0f2ebc0d69f8" width="220">
    </td>
  </tr>
</table>

## Download

<p align="center">
  <a href="https://github.com/Anxhul10/contest-notifier-apk/releases/download/1.4.2/contest-notifier.1.4.2.apk">
    <img src="https://img.shields.io/badge/Download%20Now-APK%20v1.4.2-brightgreen?style=for-the-badge&logo=android" alt="Download Contest Notifier" />
  </a>
</p>

You can also browse all previous versions on the [GitHub Releases page](https://github.com/Anxhul10/contest-notifier-apk/releases).


## Features

- **Multi-Platform Contest Tracking:** Find upcoming coding contests from Codeforces, CodeChef, and LeetCode in one place.
- **1-Hour Reminders:** Get notified one hour before a contest starts so you have time to prepare.
- **10-Minute Reminders:** Receive another notification 10 minutes before the contest begins.
- **Local Notifications:** Contest reminders are scheduled directly on your Android device using AlarmManager. No continuous server monitoring or push notification service is required.
- **Offline Contest Caching:** Contest information is stored locally using Room, allowing the app to load previously fetched contest data without waiting for the server.
- **Automatic Update Checker:** Check whether a newer app version is available and get notified when an update is released.
- **Contest Countdown:** See how much time remains before an upcoming contest starts.
- **Dark & Light Themes:** Switch between dark and light mode.
- **Simple Interface:** Browse upcoming contests through a clean and easy-to-use interface.


## Why I Built This

As someone who participates in competitive programming, I sometimes register for coding contests but sometimes forgets to actually attend them.

I might register on Codeforces, LeetCode, or CodeChef, go about my day, and completely forget that a contest is scheduled. Sometimes, I only remember after the contest has already ended!

I wanted a simple solution that would take care of remembering contest schedules for me.

That's why I built **Contest Notifier**. It brings contests from different platforms into one place and sends reminders before they start, helping me stay consistent with competitive programming.

I built this primarily to solve my own problem, but I hope it helps other programmers as well.

## API Documentation

Contest Notifier uses APIs to fetch contest information from Codeforces, CodeChef, and LeetCode.

You can explore the API endpoints and their documentation here:

**[View API Documentation](https://documenter.getpostman.com/view/36479095/2sBYHYuJEF)**

## Contributions

Contributions, suggestions, and bug reports are welcome! If you encounter an issue or have an idea for improving Contest Notifier, feel free to open an issue or contribute to the repository.

If you find this project useful, consider giving it a ⭐ on GitHub!
