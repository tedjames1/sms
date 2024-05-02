package svc

import (
	"smsserver/internal/config"
	"smsserver/pkg/memstore"
	"smsserver/pkg/store"
)

type ServiceContext struct {
	Config config.Config
	Store  store.Store
}

func NewServiceContext(c config.Config) *ServiceContext {
	return &ServiceContext{
		Config: c,
		Store:  memstore.NewMemStore(c.MaxStore),
	}
}
