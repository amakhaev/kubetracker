// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  // baseApiUrl: "http://10.0.9.105:6547/kubetracker",
  baseApiUrl: "http://localhost:6547/kubetracker",

  urls: {
    settings: "/settings",
    pods: "/pods",
    filters: "/filters",
  },

  navigation: {
    dashboard: "/dashboard",
    settings: "/settings",
    filters: "/filters",
    swagger: "http://localhost:6547/swagger-ui.html"
  }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.