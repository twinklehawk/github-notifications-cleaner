# github-notifications-cleaner
Tool to mark notifications for merged or closed PRs as done

## Settings
The tool can be configured by setting environment variables or by creating an
application.yaml file located in the same directory as the github notifications
cleaner jar. 

| yaml key        | Environment Variable                   | Description                                                                                                                                                                        | Required | Default Value           |
|-----------------|----------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|-------------------------|
| application     |                                        |                                                                                                                                                                                    |          |                         |
| - notifications |                                        |                                                                                                                                                                                    |          |                         |
| -- read         | APPLICATION_NOTIFICATIONS_READ         | If true, also process notifications that have been marked as read. Otherwise, only unread notifications are processed.                                                             | false    | true                    |
| -- sinceOffset  | APPLICATION_NOTIFICATIONS_SINCE_OFFSET | If set, only notifications modified since this offset will be processed.                                                                                                           | false    |                         |
| - github        |                                        |                                                                                                                                                                                    |          |                         |
| -- baseUrl      | APPLICATION_GITHUB_BASE_URL            |                                                                                                                                                                                    | true     | https://api.github.com  |
| -- apiToken     | APPLICATION_GITHUB_API_TOKEN           | The API token necessary to authenticate to the GitHub API. This token must be a classic token and have full repo access (necessary for pulling PR status) and notifications access | true     |                         |
