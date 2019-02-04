export const environment = {
  production: true,
  baseApiUrl: "http://10.0.9.105:6547/kubetracker",

  urls: {
    settings: "/settings",
    pods: "/pods",
    filters: "/filters",
    jenkinsJobUiTest: "/jenkins/ui_test",
    jenkinsJobActiveBuilds: "/jenkins/active_builds"
  },

  navigation: {
    dashboard: "/dashboard",
    settings: "/settings",
    filters: "/filters",
    swagger: "http://10.0.9.105:6547/swagger-ui.html"
  }
};
