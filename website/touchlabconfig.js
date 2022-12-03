const orgName = "touchlab";
const repoName = "CrashKiOS";

const docusaurusConfig = {
    title: "CrashKiOS",
    tagline: "Symbolicated crash reporting for Kotlin Multiplatform Mobile, supporting Firebase Crashlytics and Bugsnag.",
    url: `https://crashkios.touchlab.co/`,
    baseUrl: `/`,
    organizationName: orgName, // Usually your GitHub org/user name.
    projectName: repoName, // Usually your repo name.

};

const extraConfig = {
    trackingID: 'G-2F7SVSTJQS',
}

module.exports = {
    docusaurusConfig: docusaurusConfig,
    extraConfig: extraConfig
};