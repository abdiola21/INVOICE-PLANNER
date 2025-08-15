import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app/app.module';

platformBrowserDynamic().bootstrapModule(AppModule, {
  ngZoneEventCoalescing: true,
})
  .catch(err => console.error(err));


  ///Donc en fait ce que tu vas faire maintenant c'est le resume complet et detaille de notre projet pour ma prochiane discussion GPT , et comme tu sais bien que c'est une IA generative comme toi fis en sorte qu'elle comprenne sa mission s'il te plait ! 