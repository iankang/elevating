# loan-repayment
spring boot api for simulating an elevator system

## setup
for end to end setup:

```sh
cd elevating
sudo docker-compose up -d .
```
for db setup only:
```sh
cd elevating
sudo docker-compose up -d dbPostgresql
```
The application is exposed on port `8092`

To access the swagger-ui documentation, navigate to `http://localhost:8092/swagger-ui/index.html`
