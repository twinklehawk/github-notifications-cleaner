FROM gcr.io/distroless/java21-debian12:nonroot@sha256:93eecaa3e3bee98da6795700edbcdc93d6bdcb7038c44be3ab5940e24e7b620f

LABEL org.opencontainers.image.title=github-notifications-cleaner
LABEL org.opencontainers.image.url=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.source=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.description="GitHub Notifications Cleaner"
LABEL org.opencontainers.image.licenses=Apache-2.0

WORKDIR /app
COPY build/libs/app.jar app.jar

ENTRYPOINT ["/usr/bin/java", "-Dspring.aot.enabled=true", "-jar"]
CMD ["app.jar"]
