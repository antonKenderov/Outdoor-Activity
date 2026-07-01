package com.outdoor.activity.services;

import com.outdoor.activity.models.DailyForecast;
import com.outdoor.activity.models.NotificationConfig;
import com.outdoor.activity.utils.NotificationConfigParser;
import com.outdoor.activity.utils.SportEvaluator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationConfigParser notificationConfigParser;
    private final WeatherService weatherService;
    private final SportEvaluator sportEvaluator;
    private final EmailService emailService;

    @Value("${notification.latitude}")
    private double latitude;

    @Value("${notification.longitude}")
    private double longitude;

    public NotificationService(NotificationConfigParser notificationConfigParser, WeatherService weatherService, SportEvaluator sportEvaluator, EmailService emailService) {
        this.notificationConfigParser = notificationConfigParser;
        this.weatherService = weatherService;
        this.sportEvaluator = sportEvaluator;
        this.emailService = emailService;
    }

    @Scheduled(fixedRateString = "${notification.check-interval-ms}")
    public void checkAndNotify() {
        List<NotificationConfig> configs = notificationConfigParser.getAllConfigs();

        for (NotificationConfig config : configs) {

            try {
                List<DailyForecast> forecasts = weatherService.getSuitableForecast(
                        config.getSport(), latitude, longitude);

                List<DailyForecast> matching = sportEvaluator.filterByNotificationCriteria(
                        forecasts, config);

                if (!matching.isEmpty()) {
                    String subject = "Идеално време за " + config.getSport();
                    String htmlBody = buildHtmlBody(config.getSport(), matching);

                    emailService.sendEmail(config.getEmail(), subject, htmlBody);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String buildHtmlBody(String sport, List<DailyForecast> matching) {
        StringBuilder datesHtml = new StringBuilder();

        for (DailyForecast forecast : matching) {
            datesHtml.append("""
            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background: #f8fafc; border: 0.5px solid #e2e8f0; border-radius: 8px; margin-bottom: 8px;">
              <tr>
                <td style="padding: 0.875rem 1rem; font-size: 14px; font-weight: 500; color: #1e293b;">%s
                  <div style="font-size: 12px; font-weight: normal; color: #64748b; margin-top: 2px;">%s</div>
                </td>
                <td align="right" style="padding: 0.875rem 1rem;">
                  <span style="background: #dcfce7; color: #166534; font-size: 11px; font-weight: 500; padding: 3px 8px; border-radius: 4px; white-space: nowrap;">Подходящо</span>
                </td>
              </tr>
            </table>
            """.formatted(forecast.getDate(), forecast.getHours()));
        }

        return """
        <div style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif; max-width: 600px; margin: 0 auto; padding: 1rem 0;">
          <div style="background: #ffffff; border-radius: 12px; border: 0.5px solid #e2e8f0; overflow: hidden;">
            <div style="background: #1D9E75; padding: 2rem 1.5rem;">
              <span style="font-size: 22px; font-weight: 500; color: white;">Подходящи условия за спорт</span>
              <p style="color: rgba(255,255,255,0.8); margin: 0; font-size: 14px; margin-top: 4px;">Открихме подходящи дни за твоята активност</p>
            </div>

            <div style="padding: 1.5rem;">
              <div style="background: #dcfce7; border: 0.5px solid #bbf7d0; border-radius: 8px; padding: 1rem; text-align: center; margin-bottom: 1.5rem;">
                <p style="font-size: 12px; color: #166534; margin: 4px 0 2px;">Спорт</p>
                <p style="font-size: 15px; font-weight: 500; color: #166534; margin: 0;">%s</p>
              </div>

              <p style="font-size: 13px; font-weight: 500; color: #64748b; margin: 0 0 0.75rem; text-transform: uppercase; letter-spacing: 0.05em;">Подходящи дати</p>

              <div style="margin-bottom: 1.5rem;">
                %s
              </div>

              <div style="background: #f8fafc; border: 0.5px solid #e2e8f0; border-radius: 8px; padding: 0.875rem 1rem; margin-bottom: 0.5rem;">
                <p style="font-size: 12px; color: #94a3b8; margin: 0;">
                  Прогнозата се актуализира автоматично. Проверете отново преди излизане.
                </p>
              </div>

            </div>
          </div>
        </div>
        """.formatted(sport, datesHtml.toString());
    }
}
