node default {
  
	# Virtual Machines
	class { 'java7': }

	apt::source { 'datastax_community':
	  location   => 'http://debian.datastax.com/community',
	  release => '',
	  repos      => 'stable main',
	  key_source => 'http://debian.datastax.com/debian/repo_key'
	}

	package { 'dsc20':
		ensure => 'latest',
		install_options => '--force-yes',
		require => Apt::Source['datastax_community']
	}

	package { 'libssl0.9.8':
		install_options => '--force-yes',
		ensure => 'present'
	}

	package { 'python-openssl':
		install_options => '--force-yes',
		ensure => 'present'
	}

	package { 'opscenter-free':
		ensure => 'latest',
		install_options => '--force-yes',
		require => Apt::Source['datastax_community']
	}

	package { 'datastax-agent':
		ensure => 'latest',
		install_options => '--force-yes',
		require => Apt::Source['datastax_community']
	}

	
}