FROM gcr.io/distroless/java21-debian12:nonroot@sha256:a801e7ccb0606399ae950b0010b03261d4cee3d9866aa2930de6e0dcb4a5b0f5

LABEL org.opencontainers.image.title=github-notifications-cleaner
LABEL org.opencontainers.image.url=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.source=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.description="GitHub Notifications Cleaner"
LABEL org.opencontainers.image.licenses=Apache-2.0

WORKDIR /app
COPY build/libs/app.jar app.jar

ENTRYPOINT ["/usr/bin/java", "-Dspring.aot.enabled=true", "-jar"]
CMD ["app.jar"]
