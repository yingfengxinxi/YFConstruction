import ElementUi, {
  Table
} from 'element-ui'

import './src/style.css'
import './src/table-body.extend'
import tableMixins from './src/table.mixins'

if (!Table.mixins) {
  Table.mixins = []
}

Table.mixins.push(tableMixins)
