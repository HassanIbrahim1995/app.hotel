# Shift Manager Kubernetes Deployment Guide

## Overview
This directory contains the Kubernetes deployment configuration for the Shift Manager application, using Kustomize for environment-specific customizations.

## Directory Structure
```
./k8s/
├── base/                 # Base configurations for all environments
│   ├── api/              # API service base configuration
│   ├── postgres/         # PostgreSQL database base configuration
│   ├── web-client/       # Web client base configuration
│   ├── kustomization.yaml
│   └── namespace.yaml
├── overlays/             # Environment-specific configurations
│   ├── dev/              # Development environment
│   │   ├── api/
│   │   ├── postgres/
│   │   ├── web-client/
│   │   ├── kustomization.yaml
│   │   └── namespace.yaml
│   └── prod/             # Production environment
│       ├── api/
│       ├── postgres/
│       ├── web-client/
│       ├── kustomization.yaml
│       └── namespace.yaml
└── README.md
```

## Prerequisites

- Kubernetes cluster (v1.22+)
- kubectl CLI configured to access your cluster
- Kustomize (v4.0+) - included with kubectl v1.14+
- Ingress controller installed (nginx recommended)
- Persistent volume provisioner for database storage
- Optional: cert-manager for TLS certificates

## Deployment Instructions

### 1. Build and Push Docker Images

Build the Docker images and push them to your container registry:

```bash
# Set your container registry URL
export DOCKER_REGISTRY=your-registry.example.com

# Build and push API image
docker build -t $DOCKER_REGISTRY/shift-manager-api:latest -f docker/Dockerfile-api .
docker push $DOCKER_REGISTRY/shift-manager-api:latest

# Build and push Web Client image
docker build -t $DOCKER_REGISTRY/shift-manager-web:latest -f docker/Dockerfile-web .
docker push $DOCKER_REGISTRY/shift-manager-web:latest
```

### 2. Deploy to Development Environment

```bash
# Preview the generated resources (optional)
kubectl kustomize k8s/overlays/dev

# Apply all resources
kubectl apply -k k8s/overlays/dev
```

### 3. Deploy to Production Environment

```bash
# Preview the generated resources (optional)
kubectl kustomize k8s/overlays/prod

# Apply all resources
kubectl apply -k k8s/overlays/prod
```

## Environment-Specific Customizations

Kustomize allows us to customize deployments for different environments without duplicating configuration:

### Development Environment
- Reduced resource requirements
- Single replica of each service
- Uses development-specific domain (dev.shift-manager.example.com)
- SPRING_PROFILES_ACTIVE=dev for API

### Production Environment
- Higher resource limits
- Multiple replicas for high availability
- Uses production domain (shift-manager.example.com)
- SPRING_PROFILES_ACTIVE=prod for API

## Managing Secrets

Database secrets are stored as Kubernetes Secrets. In a real production environment, consider using a solution like Sealed Secrets, Vault, or a cloud provider's secret management service.

To update secrets:

```bash
# Create a new secret file
cat <<EOF > new-secret.yaml
apiVersion: v1
kind: Secret
metadata:
  name: postgres-secrets
  namespace: shift-manager-prod
type: Opaque
data:
  POSTGRES_USER: $(echo -n "your-username" | base64)
  POSTGRES_PASSWORD: $(echo -n "your-secure-password" | base64)
EOF

# Apply the new secret
kubectl apply -f new-secret.yaml

# Don't forget to delete the temporary file
rm new-secret.yaml
```

## Persistent Data

PostgreSQL data is stored in a PersistentVolumeClaim. Ensure your cluster has appropriate storage provisioning and backup strategies.

## Monitoring

You can add Prometheus ServiceMonitor resources to the Kustomize configuration for monitoring. The Spring Boot API includes Actuator endpoints that can be scraped by Prometheus.

## Troubleshooting

### Check Resource Status

```bash
# Check pods in the development namespace
kubectl get pods -n shift-manager-dev

# Check pods in the production namespace
kubectl get pods -n shift-manager-prod
```

### View Logs

```bash
# View API logs in development
kubectl logs -l app=api -n shift-manager-dev

# View Web Client logs in production
kubectl logs -l app=web-client -n shift-manager-prod
```

### Debug Database Issues

```bash
# Get PostgreSQL pod name
PG_POD=$(kubectl get pod -l app=postgres -n shift-manager-prod -o jsonpath='{.items[0].metadata.name}')

# Connect to PostgreSQL
kubectl exec -it $PG_POD -n shift-manager-prod -- psql -U shiftmanager -d shiftmanager
```

## CI/CD Integration

This Kustomize setup can be easily integrated with CI/CD pipelines. Sample GitHub Actions workflow:

```yaml
name: Deploy to Kubernetes

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build and push API image
        uses: docker/build-push-action@v4
        with:
          context: .
          file: docker/Dockerfile-api
          push: true
          tags: ${{ secrets.DOCKER_REGISTRY }}/shift-manager-api:${{ github.sha }}

      - name: Build and push Web Client image
        uses: docker/build-push-action@v4
        with:
          context: .
          file: docker/Dockerfile-web
          push: true
          tags: ${{ secrets.DOCKER_REGISTRY }}/shift-manager-web:${{ github.sha }}

      - name: Deploy to Kubernetes
        uses: actions-hub/kubectl@v1.21.0
        env:
          KUBE_CONFIG: ${{ secrets.KUBE_CONFIG }}
          IMAGE_TAG: ${{ github.sha }}
          DOCKER_REGISTRY: ${{ secrets.DOCKER_REGISTRY }}
        with:
          args: apply -k k8s/overlays/prod
```
