package store

import "context"

type Store interface {
	Put(ctx context.Context, phone string, content string) error
	Get(ctx context.Context, phone string, count int) ([]string, error)
}
