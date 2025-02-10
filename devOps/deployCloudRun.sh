gcloud run deploy daftar-back \
  --image gcr.io/daftar-360022/daftar-back \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated

gcloud run deploy daftar-front \
  --image gcr.io/daftar-360022/daftar-front \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated