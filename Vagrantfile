# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box = "crawler-saucy-vbox"

  config.vm.box_url = "http://puppet-vagrant-boxes.puppetlabs.com/ubuntu-1310-x64-virtualbox-puppet.box"

  config.vm.synced_folder ".", "/usr/src/crawler", :create => "true"

  config.hostmanager.enabled = true
  config.hostmanager.manage_host = true
  config.hostmanager.ignore_private_ip = false
  config.hostmanager.include_offline = true

  config.vm.hostname = "crawler.local"

  config.vm.provider :virtualbox do |v, override|
    override.vm.network :private_network, ip: "192.168.80.137"
    v.customize ["modifyvm", :id, "--memory", 1536]
  end

  config.vm.provision :shell, :path => "shell/initial-setup.sh"
  config.vm.provision :shell, :path => "shell/update-puppet.sh"
  config.vm.provision :shell, :path => "shell/librarian-puppet-vagrant.sh"

  config.vm.provision :puppet do |puppet|
    puppet.manifests_path = "puppet/manifests"
    puppet.options = [
      "--verbose",
      "--debug",
      "--modulepath=/etc/puppet/modules:/usr/src/crawler/puppet/modules"
    ]
  end
end
