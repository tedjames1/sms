package memstore

import (
	"context"
	"math"
	"time"

	"smsserver/pkg/store"
	"smsserver/pkg/xtime"

	"github.com/zeromicro/go-zero/core/logx"
	"github.com/zeromicro/go-zero/core/threading"
)

type memStore struct {
	max   int
	store map[string][]string
}

func NewMemStore(max int) store.Store {
	if max < 10 {
		max = 10
	}

	m := &memStore{store: make(map[string][]string), max: max}

	if err := startMonitor(m.store); err != nil {
		panic(err)
	}

	return m
}

func startMonitor(s map[string][]string) error {

	monitorTime := xtime.CurFormatedTime()

	threading.GoSafe(func() {

		for {
			select {
			case <-time.After(5 * time.Second):

				for k, v := range s {
					logx.Infof("%v - key:%v value:%v", monitorTime, k, v)
				}
			}
		}
	})

	return nil
}

// Put 数据插入到前面
func (p *memStore) Put(ctx context.Context, phone string, content string) error {

	p.store[phone] = append([]string{content}, p.store[phone]...)

	if len(p.store[phone]) > p.max { // 超长去掉末尾
		p.store[phone] = p.store[phone][:p.max]
	}

	return nil
}

// Get 从头取 N 个
func (p *memStore) Get(ctx context.Context, phone string, count int) ([]string, error) {

	vs, ok := p.store[phone]
	if !ok {
		return nil, nil
	}

	canCount := int(math.Min(float64(count), float64(len(p.store[phone]))))

	return vs[:canCount], nil
}
