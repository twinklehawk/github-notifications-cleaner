FROM gcr.io/distroless/java21-debian12:nonroot@sha256:c2353aa67320df40e4975277210286c20edee9d5dbd92d8158f491a2fbc6a261

LABEL org.opencontainers.image.title=github-notifications-cleaner
LABEL org.opencontainers.image.url=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.source=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.description="GitHub Notifications Cleaner"
LABEL org.opencontainers.image.licenses=Apache-2.0

WORKDIR /app
COPY build/libs/app.jar app.jar

CMD ["app.jar"]
