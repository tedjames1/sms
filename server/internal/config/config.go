package config

import "github.com/zeromicro/go-zero/rest"

type Config struct {
	rest.RestConf
	Password string `json:"password,default=123456"`
	MaxStore int
}
