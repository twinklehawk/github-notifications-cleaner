FROM gcr.io/distroless/java21-debian12:nonroot@sha256:610f68e186d9a190d13d19d47ffa379daf63035fb5b80e17d87d8e44074f5dad

LABEL org.opencontainers.image.title=github-notifications-cleaner
LABEL org.opencontainers.image.url=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.source=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.description="GitHub Notifications Cleaner"
LABEL org.opencontainers.image.licenses=Apache-2.0

WORKDIR /app
COPY build/libs/app.jar app.jar

CMD ["app.jar"]
