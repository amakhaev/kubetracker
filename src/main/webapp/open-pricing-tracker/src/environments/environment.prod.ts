export const environment = {
  production: true,
  baseApiUrl: "http://10.0.9.238:6545/kubetracker",

  urls: {
    settings: "/settings",
    pods: "/pods",
    filters: "/filters",
    jenkinsJobUiTest: "/jenkins/ui_test",
    jenkinsJobActiveBuilds: "/jenkins/active_builds",
    jenkinsJobLastBuilds: "/jenkins/completed_builds"
  },

  navigation: {
    dashboard: "/dashboard",
    settings: "/settings",
    filters: "/filters",
    swagger: "http://10.0.9.238:6545/swagger-ui.html"
  }
};
