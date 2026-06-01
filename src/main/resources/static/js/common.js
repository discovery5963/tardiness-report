window.Common = {
  setupLogoutOnUnload() {
    window.addEventListener('pagehide', (event) => {
      if (!event.persisted) {
        navigator.sendBeacon("/forced_logout", "");
      }
    });
  }
};