import { Component, OnInit, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MsalBroadcastService, MsalService } from '@azure/msal-angular';
import { EventMessage, EventType, InteractionStatus } from '@azure/msal-browser';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  private readonly msalService = inject(MsalService);
  private readonly broadcastService = inject(MsalBroadcastService);

  ngOnInit(): void {
    this.msalService.instance.initialize().then(() => {
      this.msalService.instance.handleRedirectPromise();
    });

    // Si el login redirect termina y no hay cuenta activa seteada, se activa la primera
    // (necesario para que idTokenClaims.roles este disponible de inmediato en el resto de la app).
    this.broadcastService.msalSubject$
      .pipe(filter((msg: EventMessage) => msg.eventType === EventType.LOGIN_SUCCESS))
      .subscribe(() => {
        const accounts = this.msalService.instance.getAllAccounts();
        if (accounts.length > 0 && !this.msalService.instance.getActiveAccount()) {
          this.msalService.instance.setActiveAccount(accounts[0]);
        }
      });

    this.broadcastService.inProgress$
      .pipe(filter((status) => status === InteractionStatus.None))
      .subscribe(() => {
        const accounts = this.msalService.instance.getAllAccounts();
        if (accounts.length > 0 && !this.msalService.instance.getActiveAccount()) {
          this.msalService.instance.setActiveAccount(accounts[0]);
        }
      });
  }
}