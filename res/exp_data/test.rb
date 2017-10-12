require 'set'
set = Set.new
File.open('200User_train_ratings.dat', 'r:utf-8') do |f|
  f.each_line do |line|
    sp = line.split("::")
    set.add(sp[1]) # movie ID
  end
end
p set.size()
