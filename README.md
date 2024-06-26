# OpenSoundQueue 
![Project Status: Finished](https://img.shields.io/badge/Project%20Status-Finished-%239747FF?style=for-the-badge&labelColor=%23191E2D&color=%239747FF) ![Visitors](https://api.visitorbadge.io/api/visitors?path=https%3A%2F%2Fgithub.com%2FOpenSoundQueue%2FOpenSoundQueue&labelColor=%23191e2d&countColor=%239747ff)

OpenSoundQueue is a cutting-edge, open-source application that transforms group music listening. Built with a modular architecture, it's robust and adaptable, ready for future updates to meet users' evolving needs.

<img src='readme_assets/Logo_With_Text_Dark.png' width='800' alt="OpenSoundQueue Logo"/>

https://github.com/OpenSoundQueue/OpenSoundQueue/assets/39237785/8393c432-c8c9-4d2b-b644-c8f6368f942b

## Empowering Your Shared Music Experience

Perfect for private gatherings or public settings like restaurants and bars, OpenSoundQueue enhances music sharing with
unique features:

| Feature                  | Description                                                                                                   |
|--------------------------|---------------------------------------------------------------------------------------------------------------|
| **Multiple Sources**     | Integrates tracks from platforms like YouTube and SoundCloud into a unified queue for varied music selection. |
| **Security & Privacy**   | Offers public and private queues with access control, ensuring session privacy and participant exclusivity.   |
| **Moderation & Control** | Equipped with moderation tools for balanced music selection and host control over the queue.                  |
| **Customizability**      | Provides extensive customization and encourages global developer contributions via GitHub.                    |
| **Multilingual Support** | Supports multiple languages for an inclusive, accessible user experience.                                     |

OpenSoundQueue delivers a memorable shared music experience, offering the flexibility to tailor the music environment
for any occasion.

## Badges

### Technologies Used

![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Vue.js](https://img.shields.io/badge/vuejs-%2335495e.svg?style=for-the-badge&logo=vuedotjs&logoColor=%234FC08D)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white)

### Streaming Sources Supported

![YouTube](https://img.shields.io/badge/YouTube-%23FF0000.svg?style=for-the-badge&logo=YouTube&logoColor=white)
![SoundCloud](https://img.shields.io/badge/SoundCloud-FF3300?style=for-the-badge&logo=soundcloud&logoColor=white)

### Community and Quality

![GitHub stars](https://img.shields.io/github/stars/OpenSoundQueue/OpenSoundQueue?style=for-the-badge)
![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg?style=for-the-badge)
![Code Quality](https://img.shields.io/badge/code_quality-A-brightgreen.svg?style=for-the-badge)

## Visuals

### Mockup of OpenSoundQueue

<img src='readme_assets/Mockup.png' width="800" alt="OpenSoundQueue Mockup"/>

### Example of Usage

<img src='readme_assets/Usage.png' width="500" alt="OpenSoundQueue Usage"/>

## Installation

To get OpenSoundQueue, download the latest release from the GitHub releases page:

- [GitHub Releases](https://github.com/OpenSoundQueue/OpenSoundQueue/releases)

Follow the instructions below after downloading the application from our project website or
the GitHub releases page.

1. **Unzip the Downloaded File:**

   If you've downloaded a zip file, unzip it before proceeding further.

2. **Open the Terminal or Command Prompt (CMD)**

3. **Navigate to the Project Root:**

   Navigate to the folder where you've unzipped  OpenSoundQueue. For example, if you've downloaded and extracted OpenSoundQueue to `Downloads/OpenSoundQueue`, you would enter:

    ```batch
    cd Downloads\OpenSoundQueue
    ```

4. **Run the Installation Command:**

    ```batch
    .\setup.bat
    ```
   Make sure to navigate to the project root directory before running this command, as it is relative to the root
   of the project.

5. **Follow the Web Installer Instructions:**

   After executing the installation script, a web installer will launch in your default web browser. Follow the
   on-screen instructions to complete the setup of OpenSoundQueue. This may include setting up an administrator account,
   configuring your music sources, and customizing application settings.

## Usage

OpenSoundQueue is designed for communal music enjoyment, allowing groups to listen and manage a shared music queue collaboratively. It's perfect for private gatherings with friends or enhancing the atmosphere in public venues like restaurants, bars, and clubs. Tailor your music experience to fit any occasion with OpenSoundQueue.

<img src='readme_assets/User_Group.png' width='400' alt="OpenSoundQueue Logo"/>

Once the application is installed, it starts automatically. If you need to restart it after installation, you can do so by navigating to the project directory and entering the following command:

```batch
java -jar opensoundqueue.jar
```

To access the web application from another device, you need to input the IP address of the host in a web browser. You can determine it using the following PowerShell command:

```powershell
Get-NetIPAddress | Where-Object { $_.InterfaceAlias -eq 'Ethernet' -and $_.AddressFamily -eq 'IPv4' } | Select-Object IPAddress
```

Please note that you should replace "Ethernet" in the command with the actual name of your network connection.

## Support

For any issues that might occur during development or usage of the application, please refer to the GitHub issue tab.

If the issue includes personal or confidential information, please reach out to our support email:

[info@opensoundqueue.org](mailto:info@opensoundqueue.org)

## Contributing

We warmly welcome contributions from anyone interested in improving OpenSoundQueue. Whether you're fixing a bug, adding
a new feature, or improving the documentation, your help is appreciated. Here's how you can contribute:

1. **Clone the Repository**

   Start by cloning the repository to your local environment using:
   ```bash
   git clone https://github.com/OpenSoundQueue/OpenSoundQueue.git
   ```

2. **Create a New Branch**

   Create a new branch with a descriptive name for your changes:
   ```bash
   git checkout -b feature/meaningful-name
   ```
   Remember to use meaningful commit messages that clearly describe each change.

3. **Consult the Documentation**

   Documentation for both the frontend and backend is available in the `docs` folder  ([link to folder](./docs)). This is a great place to start if
   you're new to the project or looking for specific implementation details.

4. **Develop Your Feature**

   With the setup complete, you're ready to start developing. Feel free to add new features, fix existing issues, or
   improve the documentation.

5. **Submit a Pull Request**

   Once you're satisfied with your work and have thoroughly tested your changes, submit a pull request. Ensure all bugs
   are addressed and your code conforms to the project's standards. Our developers will review your contribution, run
   additional tests, and provide feedback or approve your changes.

6. **Translate OpenSoundQueue**

   We also welcome contributions to translate OpenSoundQueue into different languages. You can help by using our translation platform on Crowdin:
   [Translate OpenSoundQueue on Crowdin](https://crowdin.com/project/opensoundqueue)

By contributing to OpenSoundQueue, you're helping to create a more enjoyable and collaborative music experience for
everyone. Thank you for your support and contributions!

## Authors and acknowledgment

<img src='readme_assets/Authors.png' width='800' alt="Dev-Team of OpenSoundQueue"/>

### Connect with the authors on LinkedIn:

- [![Markus Wizany](https://img.shields.io/badge/-Markus_Wizany-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/markus-wizany/)](https://www.linkedin.com/in/markus-wizany/)
- [![Daniel Pillwein](https://img.shields.io/badge/-Daniel_Pillwein-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/daniel-pillwein/)](https://www.linkedin.com/in/daniel-pillwein/)
- [![Lukas Schodl](https://img.shields.io/badge/-Lukas_Schodl-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/lukas-schodl/)](https://www.linkedin.com/in/lukas-schodl/)

## License

This project is licensed under the Apache License, Version 2.0. Copyright © 2023 Lukas Schodl. All rights reserved.

Usage of the files and contributions to the repository are subject to the following conditions:

- The software is provided "AS IS", without warranty of any kind, express or implied.
- The full text of the license can be found at [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

By using or contributing to this project, you agree to abide by its terms.
