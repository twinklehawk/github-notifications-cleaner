FROM gcr.io/distroless/java21-debian12:nonroot@sha256:5d281651dbf431f0fa9bc28bce5514a5594d66ccd2f9bf8602adccfc2518d4f9

LABEL org.opencontainers.image.title=github-notifications-cleaner
LABEL org.opencontainers.image.url=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.source=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.description="GitHub Notifications Cleaner"
LABEL org.opencontainers.image.licenses=Apache-2.0

WORKDIR /app
COPY build/libs/app.jar app.jar

CMD ["app.jar"]
