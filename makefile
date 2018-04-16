# DOCKER TASKS
# Build the container
build-image: ## Build the container
	docker build -t arturvt/kalah  -f docker/Dockerfile .


run: ## Run container on port 8080
	docker run -i -t --rm -p=8080:8080  arturvt/kalah