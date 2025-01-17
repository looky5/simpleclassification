import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'

Vue.config.productionTip = false
Vue.use(vuetify)
Vue.prototype.$baseurl = "http://localhost:8082/DataClassification";
new Vue({
  router,
  vuetify,
  render: h => h(App),
}).$mount('#app')
