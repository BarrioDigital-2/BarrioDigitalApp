import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MSAL_INSTANCE, MsalBroadcastService, MsalService } from '@azure/msal-angular';
import { PublicClientApplication } from '@azure/msal-browser';
import { App } from './app';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        provideRouter([]),
        {
          provide: MSAL_INSTANCE,
          useValue: new PublicClientApplication({
            auth: {
              clientId: 'test-client-id',
              authority: 'https://login.microsoftonline.com/common',
            },
          }),
        },
        MsalService,
        MsalBroadcastService,
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render router outlet', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('router-outlet')).toBeTruthy();
  });
});
