export function unRegister() {
  if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
      navigator.serviceWorker
        .register('/service-worker.js')
        .then(registration => {
          console.log('Service Worker registrado com sucesso:', registration)
        })
        .catch(error => {
          console.log('Erro ao registrar Service Worker:', error)
        })
    })
  }
}