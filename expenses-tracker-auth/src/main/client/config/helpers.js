var path = require('path');
var _root = path.resolve(__dirname, '..');
var _target = path.resolve(__dirname, '..', '..', '..', '..', 'target', 'classes', 'static');

exports.root = function(args) {
  args = Array.prototype.slice.call(arguments, 0);
  return path.join.apply(path, [_root].concat(args));
};

exports.target = function(args) {
    args = Array.prototype.slice.call(arguments, 0);
    return path.join.apply(path, [_target].concat(args));
};
