FROM docker.io/bellsoft/hardened-liberica-native-image-kit-container:jdk-21-nik-23-glibc AS builder

WORKDIR /build
COPY ./ /build
RUN ./gradlew nativeCompile

FROM gcr.io/distroless/static-debian13:nonroot

LABEL org.opencontainers.image.title=github-notifications-cleaner
LABEL org.opencontainers.image.url=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.source=https://github.com/twinklehawk/github-notifications-cleaner
LABEL org.opencontainers.image.description="GitHub Notifications Cleaner"
LABEL org.opencontainers.image.licenses=Apache-2.0

WORKDIR /app
COPY --from=builder /build/app/build/native/nativeCompile/app app

ENTRYPOINT ["app"]
